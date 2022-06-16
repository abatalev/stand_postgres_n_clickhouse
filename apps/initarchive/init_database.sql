CREATE TABLE IF NOT EXISTS default.Actions
(
    id  String,
    ts DateTime,
    delta Int32,
    is_archive Int32 default 0
) ENGINE = MergeTree()
PARTITION BY toYYYYMMDD(ts) 
ORDER BY (id, ts);
