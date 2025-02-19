SELECT c.customerNumber, c.customerName, COUNT(o.orderNumber) AS orderCount
FROM customers c
JOIN orders o ON c.customerNumber = o.customerNumber
GROUP BY c.customerNumber
HAVING COUNT(o.orderNumber) = (
    SELECT MAX(orderCount)
    FROM (
        SELECT COUNT(orderNumber) AS orderCount
        FROM orders
        GROUP BY customerNumber
    ) AS subquery
);

SELECT c.customerNumber, c.customerName, o.orderNumber, od.productCode, od.quantityOrdered, od.priceEach
FROM customers c
JOIN orders o ON c.customerNumber = o.customerNumber
JOIN orderdetails od ON o.orderNumber = od.orderNumber
WHERE c.country = 'Germany';

SELECT e.employeeNumber, e.firstName, e.lastName, SUM(p.amount) AS totalRevenue
FROM employees e
LEFT JOIN customers c ON e.employeeNumber = c.salesRepEmployeeNumber
LEFT JOIN payments p ON c.customerNumber = p.customerNumber
GROUP BY e.employeeNumber
ORDER BY totalRevenue DESC;

SELECT DISTINCT od.productCode, p.productName
FROM orderdetails od
JOIN orders o ON od.orderNumber = o.orderNumber
JOIN products p ON od.productCode = p.productCode
WHERE o.orderDate >= '2004-12-01' AND o.orderDate < '2005-01-01';

CREATE TABLE employeedetails (
  employeeNumber INT(11) NOT NULL,
  bankAccount VARCHAR(50) NOT NULL,
  addressLine1 VARCHAR(50) NOT NULL,
  addressLine2 VARCHAR(50) DEFAULT NULL,
  city VARCHAR(50) NOT NULL,
  state VARCHAR(50) DEFAULT NULL,
  postalCode VARCHAR(15) DEFAULT NULL,
  country VARCHAR(50) NOT NULL,
  phoneNumber VARCHAR(50) NOT NULL,
  personalEmail VARCHAR(100) NOT NULL,
  PRIMARY KEY (employeeNumber),
  CONSTRAINT fk_employeeNumber FOREIGN KEY (employeeNumber) REFERENCES employees(employeeNumber)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
