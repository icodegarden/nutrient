#insert update
mysql 的 insert ... on duplicate key update ...     innodb是行锁，死锁问题官方已在5.7后续版本修复

#分区修改

-- 如果主键与分区有字段调整 ALTER TABLE ssp_appro_approval_template DROP PRIMARY KEY, ADD PRIMARY KEY(`id`,`tenant_id`,`version_type`)

ALTER TABLE ssp_appro_approval_template  PARTITION BY LIST(tenant_id)
SUBPARTITION BY KEY(version_type) SUBPARTITIONS 3 (
   PARTITION p0 VALUES IN (10000)      
);