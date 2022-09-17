// force boost to be included as header only, also on windows
#define BOOST_ALL_NO_LIB 1

#include<vector>
#include<string>
#include <boost/thread/xtime.hpp>

#include "sharedmemory.h"
#include "jnitypeconverter.h"
#include "com_toocol_termio_platform_nativefx_NativeBinding.h"

namespace ipc = boost::interprocess;

using namespace nativefx;

std::vector<std::string> names;
std::vector<shared_memory_info*>   connections;
std::vector<ipc::message_queue*>   evt_msg_queues;        // for java events that are sent to the server
std::vector<ipc::message_queue*>   evt_msg_queues_native; // for native server events sent to the java clinet
std::vector<void*> buffers;

std::vector<ipc::shared_memory_object*> shm_infos;
std::vector<ipc::mapped_region*> info_regions;
std::vector<ipc::shared_memory_object*> shm_buffers;
std::vector<ipc::mapped_region*> buffer_regions;

JNIEnv* jni_env;

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    nextKey
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_nextKey
  (JNIEnv *, jclass);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    connectTo
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_connectTo
  (JNIEnv *, jclass, jstring);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    terminate
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_terminate
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    isConnected
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_isConnected
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    sendMsg
 * Signature: (ILjava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_sendMsg
  (JNIEnv *, jclass, jint, jstring);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    processNativeEvents
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_processNativeEvents
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    resize
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_resize
  (JNIEnv *, jclass, jint, jint, jint);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    isDirty
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_isDirty
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    redraw
 * Signature: (IIIII)V
 */
JNIEXPORT void JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_redraw
  (JNIEnv *, jclass, jint, jint, jint, jint, jint);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    setDirty
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_setDirty
  (JNIEnv *, jclass, jint, jboolean);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    setBufferReady
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_setBufferReady
  (JNIEnv *, jclass, jint, jboolean);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    isBufferReady
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_isBufferReady
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    getW
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_getW
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    getH
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_getH
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    fireMousePressedEvent
 * Signature: (IDDIIJ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_fireMousePressedEvent
  (JNIEnv *, jclass, jint, jdouble, jdouble, jint, jint, jlong);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    fireMouseReleasedEvent
 * Signature: (IDDIIJ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_fireMouseReleasedEvent
  (JNIEnv *, jclass, jint, jdouble, jdouble, jint, jint, jlong);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    fireMouseClickedEvent
 * Signature: (IDDIIIJ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_fireMouseClickedEvent
  (JNIEnv *, jclass, jint, jdouble, jdouble, jint, jint, jint, jlong);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    fireMouseEnteredEvent
 * Signature: (IDDIIJ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_fireMouseEnteredEvent
  (JNIEnv *, jclass, jint, jdouble, jdouble, jint, jint, jlong);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    fireMouseExitedEvent
 * Signature: (IDDIIJ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_fireMouseExitedEvent
  (JNIEnv *, jclass, jint, jdouble, jdouble, jint, jint, jlong);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    fireMouseMoveEvent
 * Signature: (IDDIIJ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_fireMouseMoveEvent
  (JNIEnv *, jclass, jint, jdouble, jdouble, jint, jint, jlong);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    fireMouseWheelEvent
 * Signature: (IDDDIIJ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_fireMouseWheelEvent
  (JNIEnv *, jclass, jint, jdouble, jdouble, jdouble, jint, jint, jlong);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    fireKeyPressedEvent
 * Signature: (ILjava/lang/String;IIJ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_fireKeyPressedEvent
  (JNIEnv *, jclass, jint, jstring, jint, jint, jlong);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    fireKeyReleasedEvent
 * Signature: (ILjava/lang/String;IIJ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_fireKeyReleasedEvent
  (JNIEnv *, jclass, jint, jstring, jint, jint, jlong);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    fireKeyTypedEvent
 * Signature: (ILjava/lang/String;IIJ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_fireKeyTypedEvent
  (JNIEnv *, jclass, jint, jstring, jint, jint, jlong);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    getBuffer
 * Signature: (I)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_getBuffer
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    lock
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_lock__I
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    lock
 * Signature: (IJ)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_lock__IJ
  (JNIEnv *, jclass, jint, jlong);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    unlock
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_unlock
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    waitForBufferChanges
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_waitForBufferChanges
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    hasBufferChanges
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_hasBufferChanges
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    lockBuffer
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_lockBuffer
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_toocol_termio_platform_nativefx_NativeBinding
 * Method:    unlockBuffer
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_toocol_termio_platform_nativefx_NativeBinding_unlockBuffer
  (JNIEnv *, jclass, jint);
