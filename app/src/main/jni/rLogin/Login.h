#ifndef LOGIN_H
#define LOGIN_H

#include "StrEnc.h"
#include "Includes.h"
#include "curl/curl.h"
#include "json.hpp"
#include "Log.h"
#include <jni.h>
#include <string>
#include "obfuscate.h"
#include <jni.h>
#include <string>
#include <android/log.h>
#include <openssl/evp.h>
#include <openssl/pem.h>
#include <openssl/rsa.h>
#include <openssl/err.h>
#include <openssl/md5.h>
#include <openssl/sha.h>
#include <sys/stat.h>
#include <fcntl.h>

using json = nlohmann::ordered_json;
bool xConnected = false, xServerConnection = false, memek = false;
std::string g_Auth, g_Token,ts;
//std::string g_Auth, g_Token,EXP;
bool bValid = false, xEnv = false;
bool check;
//int modekey = 1;



const char *GetAndroidID(JNIEnv *env, jobject context) {
    jclass contextClass = env->FindClass("android/content/Context");
    jmethodID getContentResolverMethod = env->GetMethodID(contextClass,"getContentResolver","()Landroid/content/ContentResolver;");
    jclass settingSecureClass = env->FindClass("android/provider/Settings$Secure");
    jmethodID getStringMethod = env->GetStaticMethodID(settingSecureClass,"getString", "(Landroid/content/ContentResolver;Ljava/lang/String;)Ljava/lang/String;");

    auto obj = env->CallObjectMethod(context, getContentResolverMethod);
    auto str = (jstring) env->CallStaticObjectMethod(settingSecureClass, getStringMethod, obj,env->NewStringUTF("android_id"));
    return env->GetStringUTFChars(str, nullptr);
}

const char *GetDeviceModel(JNIEnv *env) {
    jclass buildClass = env->FindClass("android/os/Build");
    jfieldID modelId = env->GetStaticFieldID(buildClass, "MODEL","Ljava/lang/String;");

    auto str = (jstring) env->GetStaticObjectField(buildClass, modelId);
    return env->GetStringUTFChars(str, nullptr);
}

const char *GetDeviceBrand(JNIEnv *env) {
    jclass buildClass = env->FindClass("android/os/Build");
    jfieldID modelId = env->GetStaticFieldID(buildClass, "BRAND","Ljava/lang/String;");

    auto str = (jstring) env->GetStaticObjectField(buildClass, modelId);
    return env->GetStringUTFChars(str, nullptr);
}

const char *GetPackageName(JNIEnv *env, jobject context) {
    jclass contextClass = env->FindClass("android/content/Context");
    jmethodID getPackageNameId = env->GetMethodID(contextClass, "getPackageName","()Ljava/lang/String;");

    auto str = (jstring) env->CallObjectMethod(context, getPackageNameId);
    return env->GetStringUTFChars(str, nullptr);
}

const char *GetDeviceUniqueIdentifier(JNIEnv *env, const char *uuid) {
    jclass uuidClass = env->FindClass("java/util/UUID");

    auto len = strlen(uuid);

    jbyteArray myJByteArray = env->NewByteArray(len);
    env->SetByteArrayRegion(myJByteArray, 0, len, (jbyte *) uuid);

    jmethodID nameUUIDFromBytesMethod = env->GetStaticMethodID(uuidClass,"nameUUIDFromBytes","([B)Ljava/util/UUID;");
    jmethodID toStringMethod = env->GetMethodID(uuidClass, "toString","()Ljava/lang/String;");

    auto obj = env->CallStaticObjectMethod(uuidClass, nameUUIDFromBytesMethod, myJByteArray);
    auto str = (jstring) env->CallObjectMethod(obj, toStringMethod);
    return env->GetStringUTFChars(str, nullptr);
}

struct MemoryStruct {
    char *memory;
    size_t size;
};

static size_t WriteMemoryCallback(void *contents, size_t size, size_t nmemb, void *userp) {
    size_t realsize = size * nmemb;
    auto *mem = (struct MemoryStruct *) userp;

    mem->memory = (char *) realloc(mem->memory, mem->size + realsize + 1);
    if (mem->memory == nullptr) {
        return 0;
    }

    memcpy(&(mem->memory[mem->size]), contents, realsize);
    mem->size += realsize;
    mem->memory[mem->size] = 0;

    return realsize;
}

std::string CalcMD5(std::string s) {
    std::string result;

    unsigned char hash[MD5_DIGEST_LENGTH];
    char tmp[4];

    MD5_CTX md5;
    MD5_Init(&md5);
    MD5_Update(&md5, s.c_str(), s.length());
    MD5_Final(hash, &md5);
    for (unsigned char i : hash) {
        sprintf(tmp, "%02x", i);
        result += tmp;
    }
    return result;
}

std::string CalcSHA256(const std::string& s) {
    std::string result;

    unsigned char hash[SHA256_DIGEST_LENGTH];
    char tmp[4];

    SHA256_CTX sha256;
    SHA256_Init(&sha256);
    SHA256_Update(&sha256, s.c_str(), s.length());
    SHA256_Final(hash, &sha256);
    for (unsigned char i : hash) {
        sprintf(tmp, "%02x", i);
        result += tmp;
    }
    return result;

}
extern "C" JNIEXPORT jstring JNICALL native_Check(JNIEnv *env, jclass clazz, jobject mContext, jstring mUserKey, jstring mModeSelect) {
  const char* user_key = env->GetStringUTFChars(mUserKey, nullptr);
  const char* mode_select = env->GetStringUTFChars(mModeSelect, nullptr);
  std::string hwid = user_key;
  hwid += GetAndroidID(env, mContext);
  hwid += GetDeviceModel(env);
  hwid += GetDeviceBrand(env);
  std::string UUID = GetDeviceUniqueIdentifier(env, hwid.c_str());
  std::string errMsg;
  struct MemoryStruct chunk{};
  chunk.memory = (char *) malloc(1);
  chunk.size = 0;


CURL *curl;
    CURLcode res;
    curl = curl_easy_init();
    if (curl) {
        
        curl_easy_setopt(curl, CURLOPT_CUSTOMREQUEST, "POST");
        std::string asuu = OBFUSCATE("https://free-panel.gleeze.com/connect");
        char Fek[256];
        sprintf(Fek, asuu.c_str());
        curl_easy_setopt(curl, CURLOPT_URL, Fek);
        curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1);
        curl_easy_setopt(curl, CURLOPT_DEFAULT_PROTOCOL,"https");
        struct curl_slist *headers = nullptr;
        headers = curl_slist_append(headers, "Accept: application/json");
        headers = curl_slist_append(headers,"Content-Type: application/x-www-form-urlencoded");
        headers = curl_slist_append(headers, "Charset: UTF-8");
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);
        char data[4096];
        sprintf(data,OBFUSCATE("game=PUBG&user_key=%s&serial=%s"),
                user_key, UUID.c_str());
        curl_easy_setopt(curl, CURLOPT_POST, 1);
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, data);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteMemoryCallback);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void *) &chunk);
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 0);
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYHOST, 2);
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYSTATUS, 0);
        curl_easy_setopt(curl, CURLOPT_USERAGENT, "AbsoluteX/2.0");
        res = curl_easy_perform(curl);
        if (res == CURLE_OK) {
            try {
                json result = json::parse(chunk.memory);
                if (result[("status")] == true) {
                    auto token = result[("data")][("token")].get<std::string>();
                    auto rng = result[("data")][("rng")].get<time_t>();

                    if (rng + 30 > time(nullptr)) {
                        std::string auth = "PUBG";
                        auth += "-";
                        auth += user_key;
                        auth += "-";
                        auth += UUID;
                        auth += "-";
                        auth += "Vm8Lk7Uj2JmsjCPVPVjrLa7zgfx3uz9E";
                       std::string outputAuth = CalcMD5(auth);
                        g_Token = token;
                        g_Auth = outputAuth;
                        xConnected = g_Token == g_Auth;
                        xServerConnection = true;
                        memek = true;
                        xEnv = true;
                        bValid = true;

                    }
                } else {
                    errMsg = result[("reason")].get<std::string>();
                }
            } catch (json::exception &e) {
                errMsg = "{";
                errMsg += e.what();
                errMsg += "}\n{";
                errMsg += chunk.memory;
                errMsg += "}";
            } } else {
            xEnv=false;
            errMsg = curl_easy_strerror(res);
        } }
    curl_easy_cleanup(curl);
    
    env->ReleaseStringUTFChars(mUserKey, user_key);
    env->ReleaseStringUTFChars(mModeSelect, mode_select);

    return bValid ? env->NewStringUTF("OK") : env->NewStringUTF(errMsg.c_str());
}


bool signValid = false;
extern "C" JNIEXPORT void JNICALL
Java_com_happy_pro_Component_Utils_sign(JNIEnv *env, jclass, jstring signatureHash) {
    const char *hashStr = env->GetStringUTFChars(signatureHash, nullptr);
    if (strcmp(hashStr, "MUE6RTA6MjY6OEU6QjA6OUY6QjM6RUM6Nzc6NDM6NjE6Q0Y6RUQ6RjM6Qjk6REE6QTE6RDM6NTI6Nzc6NDk6QTI6MTc6RUM6QkY6RkU6RUU6ODI6RDU6REM6RjU6ODI=") == 0) {
        signValid = true;
    } else {
        int *p = nullptr;
        *p = 0;
    }
    env->ReleaseStringUTFChars(signatureHash, hashStr);
}



int REI_HttpsCanay_Closed() {
    JavaVM *vm;
    jclass clazz;
    JavaVM* java_vm = vm;
    JNIEnv* java_env = nullptr;
    jint jni_return = java_vm->GetEnv((void**)&java_env, JNI_VERSION_1_6);
    if (jni_return == JNI_ERR)
        return -1;
    jni_return = java_vm->AttachCurrentThread(&java_env, nullptr);
    if (jni_return != JNI_OK)
        return -2;
    jclass native_activity_clazz = java_env->GetObjectClass(clazz);
    if (native_activity_clazz == nullptr)
        return -3;
    jmethodID method_id = java_env->GetMethodID(native_activity_clazz, OBFUSCATE("AndroidThunkJava_RestartGame"),/*Yaser New Restart*/OBFUSCATE("()V"));
    if (method_id == nullptr)
        return -4;
    java_env->CallVoidMethod(clazz, method_id);
    jni_return = java_vm->DetachCurrentThread();
    if (jni_return != JNI_OK)
        return -5;
    return 0;
}


bool SecherREI(const std::string& folderPath) {
    struct stat buffer{};
    return (stat(folderPath.c_str(), &buffer) == 0);
}

void Detected_REIHttpCanary() {
    std::string folderPath = OBFUSCATE("/storage/emulated/0/Android/data/com.guoshi.httpcanary");
    if (SecherREI(folderPath)) {
        REI_HttpsCanay_Closed();
    }
}
void Detected_REIHttpCanary1() {
    std::string folderPath = OBFUSCATE("/storage/emulated/0/Android/data/com.guoshi.httpcanary.premium");
    if (SecherREI(folderPath)) {
        REI_HttpsCanay_Closed();
    }
}
void Detected_REIHttpCanary2() {
    std::string folderPath = OBFUSCATE("/storage/emulated/0/Android/data/com.sniffer");
    if (SecherREI(folderPath)) {
        REI_HttpsCanay_Closed();
    }
}

void Detected_REIHttpCanary3() {
    std::string folderPath = OBFUSCATE("/storage/emulated/0/Android/data/com.httpcanary.pro");
    if (SecherREI(folderPath)) {
        REI_HttpsCanay_Closed();
    } else {

    }
}


#endif


