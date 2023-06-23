1. first you should run the backend kinedb server
2. import the kinedb driver jar into the pom.xml
3. all the codes ( entity, sqls )run with the default schema(catalog) "mysql_jpa_demo"
4. kineDB is a federate DB, not support Transaction now.
5. for id field only support GenerationType.AUTO