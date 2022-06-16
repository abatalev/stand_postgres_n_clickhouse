#!/bin/sh

echo "==> init"
while ! clickhouse-client --host archive --user default  -q "SHOW databases;"; do
    echo "==> waiting for clickhouse up"
    sleep 1
done

echo "==> run init script"
clickhouse-client --host archive --user default  --queries-file /opt/archive/init_database.sql
echo "==> done."
