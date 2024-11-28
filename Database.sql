CREATE TABLE Employee (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(100) NOT NULL,
    Nickname VARCHAR(50),
    BirthDate DATE,
    Email VARCHAR(100) UNIQUE NOT NULL,
    PhoneNumber VARCHAR(15),
    Password VARCHAR(255) NOT NULL
);

CREATE TABLE DeliveryAddress (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Street VARCHAR(100) NOT NULL,
    InteriorNumber VARCHAR(10),
    OuterNumber VARCHAR(10),
    Cologne VARCHAR(50),
    PostalCode VARCHAR(10),
    City VARCHAR(50),
    Municipality VARCHAR(50),
    FederalEntity VARCHAR(50)
);

CREATE TABLE ProductType (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    ProductType VARCHAR(30) NOT NULL
);

CREATE TABLE Customer (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    ShoppingCartID INT,
    Name VARCHAR(100) NOT NULL,
    Nickname VARCHAR(50) UNIQUE NOT NULL,
    BirthDate DATE,
    Email VARCHAR(100) UNIQUE NOT NULL,
    PhoneNumber VARCHAR(15),
    Password VARCHAR(255) NOT NULL
);

CREATE TABLE Card (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    CustomerID INT,
    OwnerName VARCHAR(100) NOT NULL,
    Transmitter VARCHAR(50),
    Number VARCHAR(16) UNIQUE NOT NULL,
    Type VARCHAR(20),
    ExpirationDate DATE,
    CVV VARCHAR(4),
    FOREIGN KEY (CustomerID) REFERENCES Customer(ID)
);

CREATE TABLE `Order` (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    CustomerID INT,
    DeliveryAddressID INT,
    TotalPrice DECIMAL(10, 2) NOT NULL,
    RealizationDate DATE NOT NULL,
    Status VARCHAR(30),
    /*Status ENUM('Validacion', 'En proceso', 'Enviado', 'Entregado', 'Cancelado'),*/
    FOREIGN KEY (CustomerID) REFERENCES Customer(ID),
    FOREIGN KEY (DeliveryAddressID) REFERENCES DeliveryAddress(ID)
);

CREATE TABLE ShoppingCart (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    CustomerID INT,
    FOREIGN KEY (CustomerID) REFERENCES Customer(ID)
);

ALTER TABLE Customer ADD CONSTRAINT FK_Customer_ShoppingCart FOREIGN KEY (ShoppingCartID) REFERENCES ShoppingCart(ID);

CREATE TABLE Product (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    EmployeeID INT,
    ProductTypeID INT,
    Image VARCHAR(255),
    Name VARCHAR(100) NOT NULL,
    Status VARCHAR(20),
    UnitPrice DECIMAL(10, 2) NOT NULL,
    Quantity INT NOT NULL,
    Description VARCHAR(100),
    FOREIGN KEY (EmployeeID) REFERENCES Employee(ID),
    FOREIGN KEY (ProductTypeID) REFERENCES ProductType(ID)
);

CREATE TABLE ShoppingCart_Product (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    ShoppingCartID INT NOT NULL,
    ProductID INT NOT NULL,
    Quantity INT NOT NULL,
    Price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (ShoppingCartID) REFERENCES ShoppingCart(ID),
    FOREIGN KEY (ProductID) REFERENCES Product(ID)
);

CREATE TABLE Order_Product (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    OrderID INT NOT NULL,
    ProductID INT NOT NULL,
    Quantity INT NOT NULL,
    Price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (OrderID) REFERENCES `Order`(ID),
    FOREIGN KEY (ProductID) REFERENCES Product(ID)
);