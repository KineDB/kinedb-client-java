# kinedb-client-java
This a Java client for kinedb, user can import this jar package to connect kinedb.   
![image](https://user-images.githubusercontent.com/131343640/233318313-8f5677a0-9e56-4d73-b545-ca5e230d318b.png)
# main trait
1. use normative JDBC api  
2. connect with grpc   
3. Support mapping of KineDB and Java data types,including: Byte、Short、Integer、Long、Boolean、Float、Double、Date、Time、Timestamp、BigDecimal   
# require
1. java8 or newer
2. kinedb-jdbc-3.20.0.jar or newer
3. KineDB 3.20.0 or newer
# step
## 1. Introduce kinedb-jdbc driver jar package into the project  
```sh
<dependency>
  <groupId>com.itenebris.kinedb</groupId>
  <artifactId>kinedb-jdbc</artifactId>
  <version>3.20.0</version>
</dependency>
```
## 2.Register the driver
```sh
DriverManager.registerDriver(com.itenebris.kinedb.jdbc.KineDriver)
```
## 3.Establish a connection
```sh
Connection conn = DriverManager.getConnection(url, user, password)
```
## 4.JDBC url
```sh
jdbc:kine://host:port/database?Param1=value1&paramN=valueN
```
## 5.Execute SQL  
#### (1) Execute statement
```sh
String sql = "select * from table where id = 100";
Statement st = conn.createStatement();  
ResultSet rs = st.executeQuery(sql);
```
#### (2) Execute prepareStatement
```sh
PreparedStatement pstmt = con.prepareStatement("INSERT INTO user1 	(id ,name, age) values (?, ?, ?)");
    pstmt.setInt(1, 1011);
    pstmt.setString(2, "Jack");
    pstmt.setInt(3, 1021);
boolean result = pstmt.execute();
```
## 6.ResultSet processing
```sh
While(rs.next()){  
    rs.getString(“col_name”);  
    rs.getInt(1);  
    //…
}
```
## 7.release resources
```sh
 if (rs != null) {
         rs.close();
     }
 } catch (SQLException e) {
     e.printStackTrace();
 } finally {
     try {
         if (st != null) {
             st.close();
         }
     } catch (SQLException e) {
         e.printStackTrace();
     } finally {
         try {
             if (conn != null) {
                 conn.close();
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }
 }
```
# Mybatis Example
## 1. Configuration mybatis-config.xml
```sh
<configuration>
<environments default="development">
    	<environment id="development">
        	<transactionManager type="JDBC"/>
        	<dataSource type="POOLED">
            <property name="driver"  value="com.itenebris.kinedb.jdbc.KineDriver"/>
            <property name="url" 	value="jdbc:kine://127.0.0.1:10301/database?useServerPrepStmts=true"/>
            <property name="username" value="root"/>
            <property name="password" value=""/>
        </dataSource>
    	</environment>
 </environments>
	<mappers>
    	<mapper resource="mapper/UserMapper.xml"/>
	</mappers>
</configuration>
```
## 2. User.class
```sh
public class User {
    		private Long id;
    		private String name;
    		private Integer age;
}
```
## 3. UserMapper.xml
#### (1)search
```sh
<select id="selectById" resultMap="userResultMap">
    	select * from user1 where id = #{id}
    </select>
```
#### (2)insert
```sh
<insert id="insert" parameterType="pojo.User">
    	insert into user1 (id, name, age) values (#{id},#{name},#{age})
    </insert>
```
## 4.UserMapper API
```sh
public interface UserMapper2 {
       	User selectById(long id);
  	int insert(User user);
}
```
## 5.Execute
#### (1)search
```sh
  long id=1;
	User user = userMapper.selectById(id);
	System.out.println("testSelectById result " + user);
```
#### (2)insert
```sh
  User user = new User();
	user.setId(1L);
	user.setName("name");
	user.setAge(10);
	int result = userMapper.insert(user);
	System.out.println("testInsert result " + result);
```
# Spring boot JPA Exanple
```sh
（1）application.yml
server:
  port: 8080
spring:
  datasource:
    driverClassName: com.itenebris.kinedb.jdbc.KineDriver
    url: jdbc:kine://localhost:10301/database
    username:
    password:
  jpa:
    show-sql: true # default false, displays the SQL statements executed in the log
    hibernate.ddl-auto: none 
    database-platform: org.hibernate.dialect.MySQLDialect #currently use mysql dialect
    hibernate:
      naming:
        implicit-strategy: org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
    open-in-view: false
logging.level.org.hibernate:
                        SQL: DEBUG
                        type: TRACE
```
```sh
 (2)Entity(Company.class)

	@Entity
	@Data
	@NoArgsConstructor
	@Table(name = "company")
	public class Company extends CreateUpdateBase{
    	@Id
    	@GeneratedValue(strategy = GenerationType.AUTO, generator = "customId")
    	@GenericGenerator(name = "customId", strategy = "com.itenebris.jpa.demo.CustomIdGenerator")//need to customize the ID policy
    	private Long id;
    	@Column(name = "name")
    	private String name;
    	@Column(name = "catch_phrase")
    	private String catchPhrase;
    	@Column(name = "bs")
    	private String bs;
}
//Custom ID generator
	public class CustomIdGenerator extends IdentityGenerator {
    	@Override
    	public Serializable generate(SharedSessionContractImplementor s, Object obj) throws HibernateException {
        Serializable id = s.getEntityPersister(null, 	obj).getClassMetadata().getIdentifier(obj, s);
        log.info("CustomIdGenerator generate id:{} \n", id);
        if (id != null && Long.valueOf(id.toString()) > 0) {
            return id;
        } else {
            return super.generate(s, obj);
        }
    	}
	}
```
```sh
 (3)DAO (CompanyRepository.class)
	@Repository
	public interface CompanyRepository extends JpaRepository<Company, Long> 	{
    		Optional<Company> findByName(String name);
	}
```
```sh
(4)JPA mode execution
//insert
	Company company = new Company();
	company.setBs("BS....");
	company.setCatchPhrase("catchPhase ....");
	company.setName("company1");
	company.setId(1L);
	company.setCreatedAt(new Date());
	companyRepository.save(company);
//search
	Optional<Company> companyOptional = companyRepository.findById(1L);
```
```sh
(5)JPQL support
	@Modifying
	@Transactional
	@Query(value = "select c.* from mysql_jpa_demo.employees e join mysql_jpa_demo.company c on e.company_id = c.id", nativeQuery = true)
	List<Company> findAllCompaniesWithSql();
	search all companies
	List<Company> companies = companyRepository.findAllCompaniesWithSql();
```
