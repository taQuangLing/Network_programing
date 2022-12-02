CFLAGS = -c -Wall
CC = gcc -g
LIBS = -lm
MYSQLCFLAGS= -L/usr/local/mysql/lib
MYSQLLIBS= -lmysqlclient

all: main

main: main.o
	${CC} $(MYSQLCFLAGS) main.o $(MYSQLLIBS) -o main

main.o: main.c
	${CC} ${CFLAGS} main.c
clean:
	rm -f *.o *~