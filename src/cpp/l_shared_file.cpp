#include "l_shared_file.h"

#include <errno.h>
#include <fcntl.h>

#include <unistd.h>

#include <sys/file.h>
#include <sys/wait.h>
#include <sys/stat.h>


static int nfd = -1000;

int create_shared_pfile(const char* sfile) {
	int fd = open(sfile , O_CREAT | O_RDWR | O_EXCL , 0644);
	if(fd != 0) {
		PLOGI("[-] create shared file error %s", strerror(errno));
		return fd;
	}

	PLOGI("[*] create shared file %s sucess", sfile);
	nfd = fd;
	return fd;
}

int open_shared_pfile(const char* sfile) {
	int fd = open(sfile, O_RDONLY);
	if(fd <= 0) {
		PLOGI("[-] open shared file error %s", strerror(errno));
		return fd;
	}

	return fd;
}

int lock_shared_pfile(int fd) {
	int lock = flock(fd, LOCK_EX | LOCK_NB);
	if(lock != 0) {
		PLOGI("[*] pid %d get shared file lock failed", getpid());
	}

	return lock;
}

int unlock_shared_pfile(int fd) {
	flock(fd, LOCK_UN);
	close(fd);
	nfd = -1000;
}

int get_shared_pfile_fd() {
	return nfd;
}

