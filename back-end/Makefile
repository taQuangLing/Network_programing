CFLAGS = -c -Wall
CC = gcc -g
LIBS = -lm
MYSQLCFLAGS= -L/usr/local/mysql/lib
MYSQLLIBS= -lmysqlclient

build: clean | client server
test: test

server: server.o linklist.o AppUtils.o Data.o http.o DB.o
	${CC} server.o linklist.o AppUtils.o Data.o http.o DB.o -o server `mysql_config --cflags --libs`
client: client.o linklist.o AppUtils.o Data.o http.o DB.o
	${CC} client.o linklist.o AppUtils.o Data.o http.o DB.o -o client `mysql_config --cflags --libs`
test: test.o linklist.o AppUtils.o Data.o http.o DB.o
	${CC} test.o linklist.o AppUtils.o Data.o http.o DB.o -o test `mysql_config --cflags --libs`
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
Data.o: lib/Data.c
	${CC} ${CFLAGS} lib/Data.c
http.o: lib/http.c
	${CC} ${CFLAGS} lib/http.c
DB.o: lib/DB.c
	${CC} ${CFLAGS} lib/DB.c
clean:
	rm -f *.o *~