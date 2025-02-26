# ComputerComponentComputer
## Introduction
預期目標 :<br>
1. 程式能執行正常的CRUD
2. 說明程式架構、前後端分離、MVC、Service、DAO
3. 說明前後端程式細節(JasperReport、資料庫存取、交易)
## Tool
- Eclipse
- MySQL
## Software Architecture
- src/main/java
  - com.project.david
    - entity
      - Employee.java (class)
      - Order.java (class)
      - Product.java (class)
    - dao
      - BaseDAO.java (interface)
      - DAOException.java (class)
      - impl.jpa
        - EmployeeRepository.java (interface)
        - EmployeeDaoImpl.java (class)
        - OrderRepository.java (interface)
        - OrderDaoImpl.java (class)
        - ProductRepository.java (interface)
        - ProductDaoImpl.java (class)
    - service
      - EmployeeeService.java (interface)
      - OrderService.java (interface)
      - ProductService.java (interface)
      - ReportService.java (interface)
      - ServiceException.java (class)
      - impl
        - EmployeeServiceImpl.java (class)
        - OrderServiceImpl.java (class)
        - ProductServiceImpl.java (class)
        - ReportServiceImpl.java (class)
    - controller
      - EmployeeController.java (class)
      - RegisterLoginController.java (class)
      - OrderController.java (class)
      - ProductController.java (class)
      - ReportController.java (class)
- src/main/resources
  - static
  - template
  - application.properties
