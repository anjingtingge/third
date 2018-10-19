# The ARMv7 is significanly faster due to the use of the hardware FPU
APP_ABI := armeabi,armeabi-v7a,x86，mips,arm64-v8a，mips64,x86_64,armeabi armeabi-v7a x86
APP_PLATFORM := android-19
APP_STL :=gnustl_static
APP_CPPFLAGS :=-frtti -fexceptions