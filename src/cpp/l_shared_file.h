#ifndef __SHARED_FILE_H__
#define __SHARED_FILE_H__

#include <stdlib.h>
#include <stdio.h>
#include <string.h>


#include <android/log.h>

#define PLOG_DEBUG 1

#define PLOGV(...) ((void)__android_log_print(ANDROID_LOG_VERBOSE, "LOG", __VA_ARGS__))
#define PLOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, "LOG", __VA_ARGS__))
#define PLOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, "LOG", __VA_ARGS__))
#define PLOGW(...) ((void)__android_log_print(ANDROID_LOG_WARN, "LOG", __VA_ARGS__))

#ifdef PLOG_DEBUG
#define PLOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "LOG", __VA_ARGS__))
#endif

#ifdef __cplusplus
extern "C" {
#endif

int create_shared_pfile(const char* sfile);

int open_shared_pfile(const char* sfile);

int lock_shared_pfile(int fd);

int unlock_shared_pfile(int fd);

int get_shared_pfile_fd();

#ifdef __cplusplus
}
#endif

#endif
