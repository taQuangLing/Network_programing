CFLAGS = -c -Wall
CC = gcc -g
LIBS = -lm
MYSQLCFLAGS= -L/usr/local/mysql/lib
MYSQLLIBS= -lmysqlclient

all: test

test: test.o db_linklist.o
	${CC} $(MYSQLCFLAGS) test.o $(MYSQLLIBS) db_linklist.o -o test

test.o: test.c
	${CC} ${CFLAGS} test.c
db_linklist.o: lib/db_linklist.c
	${CC} ${CFLAGS} lib/db_linklist.c
clean:
	rm -f *.o *~