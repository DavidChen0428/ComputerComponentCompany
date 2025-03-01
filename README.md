# ComputerComponentComputer
## Introduction
預期目標 :<br>
1. 程式能執行正常的CRUD
2. 說明程式架構、前後端分離、MVC、Service、DAO
3. 說明前後端程式細節(JasperReport、資料庫存取、交易)
4. 加選 : 日誌、log4j、JWT
## Tool
- Eclipse
- MySQL
## Software Architecture
- src/main/java
  - com.project.david
    - entity
      - Employee.java (class) O
      - Order.java (class) O
      - Product.java (class) O
    - dao
      - BaseDAO.java (interface) O
      - DAOException.java (class) O
      - impl.jpa
        - EmployeeRepository.java (interface) O
        - EmployeeDaoImpl.java (class) O
        - OrderRepository.java (interface) O
        - OrderDaoImpl.java (class) O
        - ProductRepository.java (interface) O
        - ProductDaoImpl.java (class) O
    - dto
      - employee
        - EmployeeConverter.java (class) O
        - EmployeeRegisterDTO.java (class) O
        - EmployeeLoginDTO.java (class) O
        - EmployeeUpdateDTO.java (calss) O
        - EmployeeDeleteDTO.java (class) O
    - service
      - EmployeeeService.java (interface) O
      - OrderService.java (interface)  O
      - ProductService.java (interface) O
      - ReportService.java (interface)
      - ServiceException.java (class) O
      - impl
        - EmployeeServiceImpl.java (class) O
        - OrderServiceImpl.java (class) O
        - ProductServiceImpl.java (class)
        - ReportServiceImpl.java (class)
    - controller
      - EmployeeController.java (class) O
      - AuthenticationController.java (class) O
      - OrderController.java (class)
      - ProductController.java (class)
      - ReportController.java (class)
- src/main/resources
  - static
  - template
  - application.properties
