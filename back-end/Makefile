CFLAGS = -c -Wall
CC = gcc -g
LIBS = -lm
MYSQLCFLAGS= -L/usr/local/mysql/lib
MYSQLLIBS= -lmysqlclient

build: clean | client server
test: test

server: server.o linklist.o AppUtils.o MessageCode.o Data.o http.o DB.o Comment.o Contains.o File.o Follow.o Image.o Notification.o NotifyContains.o Post.o Reaction.o User.o
	${CC} server.o linklist.o AppUtils.o MessageCode.o Data.o http.o DB.o Comment.o Contains.o File.o Follow.o Image.o Notification.o NotifyContains.o Post.o Reaction.o User.o -o server `mysql_config --cflags --libs`
client: client.o linklist.o AppUtils.o MessageCode.o Data.o http.o DB.o Comment.o Contains.o File.o Follow.o Image.o Notification.o NotifyContains.o Post.o Reaction.o User.o
	${CC} client.o linklist.o AppUtils.o MessageCode.o Data.o http.o DB.o Comment.o Contains.o File.o Follow.o Image.o Notification.o NotifyContains.o Post.o Reaction.o User.o -o client `mysql_config --cflags --libs`
test: test.o linklist.o AppUtils.o MessageCode.o Data.o http.o DB.o Comment.o Contains.o File.o Follow.o Image.o Notification.o NotifyContains.o Post.o Reaction.o User.o
	${CC} test.o linklist.o AppUtils.o MessageCode.o Data.o http.o DB.o Comment.o Contains.o File.o Follow.o Image.o Notification.o NotifyContains.o Post.o Reaction.o User.o -o test `mysql_config --cflags --libs`
test.o: test.c
	${CC} ${CFLAGS} test.c
server.o: server.c
	${CC} ${CFLAGS} server.c
client.o: client.c
	${CC} ${CFLAGS} client.c
linklist.o: lib/linklist.c
	${CC} ${CFLAGS} lib/linklist.c
AppUtils.o: lib/AppUtils.c
	${CC} ${CFLAGS} lib/AppUtils.c
MessageCode.o: lib/MessageCode.c
	${CC} ${CFLAGS} lib/MessageCode.c
Data.o: structures/Data.c
	${CC} ${CFLAGS} structures/Data.c
http.o: lib/http.c
	${CC} ${CFLAGS} lib/http.c
Comment.o: structures/Comment.c
	${CC} ${CFLAGS} structures/Comment.c
Contains.o: structures/Contains.c
	${CC} ${CFLAGS} structures/Contains.c
File.o: structures/File.c
	${CC} ${CFLAGS} structures/File.c
Follow.o: structures/Follow.c
	${CC} ${CFLAGS} structures/Follow.c
Image.o: structures/Image.c
	${CC} ${CFLAGS} structures/Image.c
Notification.o: structures/Notification.c
	${CC} ${CFLAGS} structures/Notification.c
NotifyContains.o: structures/NotifyContains.c
	${CC} ${CFLAGS} structures/NotifyContains.c
Post.o: structures/Post.c
	${CC} ${CFLAGS} structures/Post.c
Reaction.o: structures/Reaction.c
	${CC} ${CFLAGS} structures/Reaction.c
User.o: structures/User.c
	${CC} ${CFLAGS} structures/User.c
DB.o: lib/DB.c
	${CC} ${CFLAGS} lib/DB.c
clean:
	rm -f *.o *~