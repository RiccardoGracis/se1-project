# Requirements Document 

Authors: Rocco Luca Iamello, Massimo Di Natale, Paolo Trungadi, Riccardo Gracis

Date: 19/04/2021

Version: 1.31

# Contents

- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces) 
	
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases](#use-cases)
    	+ [Relevant scenarios](#use-cases)
- [Glossary](#glossary)
- [System design](#system-design)
- [Deployment diagram](#deployment-diagram)

# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers 
EZShop is a software application to:
* manage sales
* manage inventory
* manage customers
* support accounting

EZShop is a clothing shop located in Turin composed by:
 - One Owner that covers the role of Administrator manager (HR,Accounting and all the support functions).
 - One Sales Manager
 - Six employees (Shift of three people that include a cashier and two shop assistants) 


# Stakeholders


| Stakeholder name  | Description | 
| ----------------- |:-----------:|
| EZShop's Administrator manager | Responsible of application and company management. | 
| Security manager | Responsible of security management |
| DB Administrator | Manages all the tables of the DB |
| Developer | Develops the SW |
| Customer's fidelity card | Entity/Person that is (indirectly) linked to a VIRTUAL fidelity card |
| EZShop's shop assistant | End user at a lower level (helps customer, signals missing items etc.) |
| EZShop's cashier | End user at a lower level (handles sales) |
| EZShop's Sales manager | Higher level user (Analyses profits, trends, supervises inventory orders etc.) |
| Analyst | Expert in requirement engneering |
| Buyer | EZShop's owner that pays for the application |
| Payment System | Module to manage non-cash payments |
| Product | Bar code identifier |

# Context Diagram and interfaces

## Context Diagram


![Alt text](Context_Diagram.png?raw=true "Context Diagram")



## Interfaces


| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
|   EZShop's shop assistant     | GUI | Touchscreen of a tablet |
|   EZShop's cashier   | GUI | Touchscreen of an All-in-one desktop |
|  EZShop's Administrator manager | GUI | Screen, Keyboard of a PC |
|  EZShop's Sales manager   | GUI | Screen, Keyboard of a PC |
|  Payment System | Axerve API Interface as described in: https://api.gestpay.it/ | POS device, internet connection |
|  Product | Bar Code identifier | Bar Code Reader |
| Customer's fidelity card  | Card number / Bar Code identifier | Card reader (Probably integrated in Bar Code reader of products) |

# Stories and personas

Mario is 24, he is a design student and works part-time at EZShop to fund his studies. When a customer approaches his line he scans the items with the bar code reader so that the system automatically computes the total cost. If an item bar code is not readable he manually insert the item’s number on his cash register device. 
When he’s done scanning all the customer’s bought items he presses a key on his screen to close the transaction while selecting the customer’s payment method (cash or cards).

Anna is 27, she works at EZShop as a shop assistant. She helps people finding the right products and suggests possible options to them. 
If a customer is looking for an item which is currently not available in the shop, she checks straight away its availability in the warehouse using her tablet. When a customer chooses his product she could bring it to the cashier to proceed with the payment.

Luca is 45, he works as sales manager for EZshop and two times per week (typically on Monday and Thursday) opens his PC in the office in order to analyse profits, trends and to have a complete view of the half week’s sales. From these analysis he selects (if needed) items to be re-ordered and quantity. He is interested in performing these tasks only on Monday and Thursday because in the other days of the week he will prepare promotions for less-sold items or he will find new interesting clothes to be sold in EZShop.

Rebecca is 40 year old, she is the EZShop owner and she covers also the Administrator Manager role. She has the company full view, she loves the Unix environement whichin she is able to manage easily all the users providing them with several types of privileges.
In the EZShop admin tools she wants to manage the employees in the same hierarchical way as in the Unix systems but in order to avoid lot of CL commands she prefers to interact with a GUI.
She wants to resolve problems involved in IT sector helping his/her colleagues, and wants to collect some feedbacks in order to improve some functionalities if needed (by contacting the SW company that developed the software).
Furthermore she manages all the contability of EZShop handling bureaucracies, based upon sales manager and DB manager advices then he/she evaluate the hirings covering also the HR role.



# Functional and non functional requirements

## Functional Requirements



| ID        | Description  |
| ------------- |:-------------:| 
|  FR1     | Manage Sale |
| FR1.1  | Open new transaction | 
| FR1.2  | Read fidelity card (if given) | 
| FR1.3  | Retrieve name and price (from barcode) | 
| FR1.4  | Close transaction | 
| FR1.4.1| Store transaction |
| FR1.5 | Apply discount/promotion (if any) |
| FR1.6 | Compute grand total | 
| FR1.7 | Find past transaction |
| FR1.8 | Delete past transaction |
| FR1.8.1 | Generate refund coupon |
| FR2  | Manage Payment |
| FR2.1 | Select Payment Method | 
| FR2.1.1 | Open drawer (if cash payment)  | 
| FR2.1.2 | Enable POS (if CC payment)  | 
| FR2.2 | Produce sale ticket | 
| FR3 | Manage inventory |
| FR3.1 | Add new item | 
| FR3.2 | Delete item |
| FR3.3 | Modify item |
| FR3.3.1 | Modify quantity/availability |
| FR3.3.2 | Modify price/active promotion |
| FR3.3.3 | Modify description |
| FR4 | Retrieve item info |
| FR4.1 | Search by name |
| FR4.2 | Search by code |
| FR4.3 | Search by category |
| FR4.4 | Display results, prices, descriptions |
| FR4.5 | Filter out according to Size, Colour, Price | 
| FR5 | Manage employee |
| FR5.1 | Add employee |
| FR5.1.1 | Define Role/Privilege |
| FR5.2 | Remove employee | 
| FR5.3 | Modify employee |
| FR6  | Manage customer |
| FR6.1 | Add new fidelity card | 
| FR6.2 | Disable fidelity card |
| FR6.3 | Find card balance |
| FR7 | Perform analysis | 
| FR7.1| Set analysis parameters | 
| FR7.2 | Produce graph |
| FR7.3 | Export graph/trend |
| FR8  | Manage accounting | 
| FR8.1 | Track expenses/incomes |
| FR8.2 | Produce Report |

## Non Functional Requirements



| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- |:-------------:| :-----:| -----:|
|  NFR1     |  Usability | Intuitive application that can be used by every employee within 2 weeks of basic training | All FRs |
|  NFR2     | Efficiency | Time to retrieve and display item info < 1 sec    | FR4 and FR1.3 |
|  NFR3     | Efficiency | Time to produce reports/graphs/analysis < 2 sec  | FR7, FR8 |
| NFR4 | Correctness | 0,01% error rate (duplicate invoice ID, bad search result) | ALL FRs | 
| NFR5 | Realibility/Availability | 99% availability rate (Few payment problems etc.) | FR1,FR2 | 
| NFR6 | Maintainability | Ordinary maintenance (patches etc) within 1-2 working days from request (if any)  | ALL FRs | 
| NFR7 | Maintainability | Functions improvement within 1 month from Owner's request (if any) | ALL FRs | 
| NFR8 | Security | Data handled following GDPR's policies | FR5, FR6, FR2 |
| NFR9 | Security | EZShop will never collect or store data about Credit Cards | FR2 |

# Use case diagram and use cases


## Use case diagram


![Alt text](UseCase_Diagram.png?raw=true "Use Case Diagram")


## Use cases

### Use Case 1, UC1 - Manage sales and payment
| Actors Involved        | Cashier |
| ------------- |:-------------:|
|  Precondition     | There must be a client who wants to buy something |
|  Post condition     | Transaction ended |
|  Nominal Scenario     | The cashier during a transaction wants to have the full control of the customer cart in order to add/remove item(s) in the cart anytime before the transaction is closed |
|  Variants     | Client could choose card payment or cash payment |

##### Scenario 1.1
| Scenario | Simple cash routine (cash payment) |
| ------------- |:-------------:|
|  Precondition     | There is a costumer with at least one item to buy |
|  Post condition     | Transaction ended |
| Step#        | Description  |
|  1     | Cashier scan using the bar code reader all the costumer's items |
|  2     | System automatically takes care of discount and displays always the updated price |
|  3     | When all the items are scanned final price is displayed |
|  4     | System leads the cashier to the payment section |
|  5     | Now cashier get the money and insert the amount in the form and system will automatically compute rest or missing amount |
|  6     | Once the payment is completed the system will print the receipt and update transaction |

##### Scenario 1.2
| Scenario | Simple cash routine (card payment) |
| ------------- |:-------------:|
|  Precondition     | There is a costumer with at least one item to buy |
|  Post condition     | Transaction ended |
| Step#        | Description  |
|  1     | Cashier scans using the bar code reader all the costumer's items |
|  2     | System automatically takes care of discount and displays always the updated price |
|  3     | When all the items are scanned final price is displayed |
|  4     | System leads the cashier to the payment section |
|  5     | Now cashier enables the POS (with specific circuit card) which will execute its own routine in order to read the card get the pin interacting directly with the client |
|  6     | Once POS has finished its operation system automatically print the receipts |

##### Scenario 1.3
| Scenario | Simple cash routine (card payment) transaction cancelled due to specific error|
| ------------- |:-------------:|
|  Precondition     | There is a costumer with at least one item to buy |
|  Post condition     | Transaction ended |
| Step#        | Description  |
|  1     | Cashier scans using the bar code reader all the costumer's items |
|  2     | System automatically takes care of discount and displays always the updated price |
|  3     | When all the items are scanned final price is displayed |
|  4     | System leads the cashier to the payment section |
|  5     | Now cashier enable the POS (with specific circuit card) which will execute its own routine in order to read the card get the pin interacting directly with the client |
|  6     | POS will raise an error with the specific description |
|  7     | System permits to re-edit the cart or retry transaction both in cash or in card mode |

##### Scenario 1.4
| Scenario | Client want to modify cart items |
| ------------- |:-------------:|
|  Precondition     | There is a client that want to modify their cart items |
|  Post condition     | Transaction ended |
| Step#        | Description  |
|  1     | Cashier scans all the costumer's items |
|  2     | Costumer wants to modify their cart adding/removing/changing some items |
|  3     | Cashier is able to modify it updating automatically all the price and eventually the sales |
|  4     | When costumer has finished all his modification Cashier can initiate the payment phase |

##### Scenario 1.5
| Scenario | Client uses a Coupon |
| ------------- |:-------------:|
|  Precondition     | Client must have a valid coupon code |
|  Post condition     | - |
| Step#        | Description  |
|  1     | Cashier scans all the costumer's items |
|  2     | Costumer before the payment operation has to present the coupon code to the cashier |
|  3     | Cashier insert (or scan) the coupon code |
|  4     | System automatically updates the final price |

##### Scenario 1.6
| Scenario | Client want to use his personal discount |
| ------------- |:-------------:|
|  Precondition     | Client must have a valid costumer card with an available discount |
|  Post condition     | - |
| Step#        | Description  |
|  1     | Cashier scans the costumer card |
|  2     | Cashier scans all the costumer's items |
|  3     | Cashier is able to get costumer informations such as the total number of points and the total price discount |
|  4     | If discount is available based on client indication cashier can enable or not the personal costumer discount |
|  5     | If the discount is accepted the system will automatically update the total price |

##### Scenario 1.7
| Scenario | Client refund an item |
| ------------- |:-------------:|
|  Precondition     | There is a costumer with a receipts emitted at most 30 days ago |
|  Post condition     | Refund Coupon emitted |
| Step#        | Description  |
|  1     | Cashier scan/insert the receipt code |
|  2     | Based on customer indication Cashier select the right item(s) to refund |
|  3     | System automatically will generate the Coupon with the total amount of the item(s) |


### Use case 2, UC2 - Add item in the inventory
| Actors Involved        | Product, (Sales Manager OR Administrator) |
| ------------- |:-------------:| 
|  Precondition     | Product does not exist in the inventory |  
|  Post condition     | Product added in the inventory |
|  Nominal Scenario     | Sales manager goes to the inventory page and adds a new product in the inventory by filling all its fields |
|  Variants     | In case one or more values of the new item are not correct an error message is displayed suggesting to insert accepted values |

### Use case 3, UC3 - Modify item in the inventory
| Actors Involved        | Product, (Sales Manager OR Administrator) |
| ------------- |:-------------:| 
|  Precondition     | Product exists in the inventory |  
|  Post condition     | - |
|  Nominal Scenario     | Sales manager goes to the Inventory page and she selects the item that wants to modify, then she writes the updated field(s) in the form |
|  Variants     | In case one or more values of the modified item are not correct an error message is displayed suggesting to insert accepted values |

### Use case 4, UC4 - Remove item from the inventory
| Actors Involved        | Product, (Sales Manager OR Administrator) |
| ------------- |:-------------:| 
|  Precondition     | Product exists in the inventory |  
|  Post condition     | Product does not exist from the inventory |
|  Nominal Scenario     | Sales manager goes to the Inventory page and removes the item |

### Use case 5, UC5 - Retrieve item info
| Actors Involved        | Shop Assistant |
| ------------- |:-------------:| 
|  Precondition     | - |  
|  Post condition     | - |
|  Nominal Scenario     | Shop assistant goes to the search section of the system in order to look for information about certain product. System queries the inventory according to shop assistant criteria and finally it displays the search results |
|  Variants     | If a product does not exist in the inventory, an error message is displayed |

##### Scenario 5.1 

| Scenario | Search by code |
| ------------- |:-------------:| 
|  Precondition     | - |
|  Post condition     | - |
| Step#        | Description  |
|  1     | Shop assistant goes to the search section of the system |  
|  2     | Shop assistant scans the code of a certain product |  
|  3     | System queries the inventory according to the code of the product |  
|  4     | System displays the page of the corresponding product, where it is possible to see information about the item (available size, name, category, brand, color, price) |

##### Scenario 5.2

| Scenario | Search by category C |
| ------------- |:-------------:| 
|  Precondition     | - |
|  Post condition     | - |
| Step#        | Description  |
|  1     | Shop assistant goes to the search section of the system |  
|  2     | Shop assistant chooses the category C to look for |
|  3     | System queries the inventory according to the category C |  
|  4     | System displays a list of items belonging to category C |
|  5 (Optional)| Shop assistant goes to the page of a specific item to see its specific information | 

##### Scenario 5.3

| Scenario | Search by name N |
| ------------- |:-------------:| 
|  Precondition     | - |
|  Post condition     | - |
| Step#        | Description  |
|  1     | Shop assistant goes to the search section of the system |  
|  2     | Shop assistant writes the name N of the product in the search bar |
|  3     | System queries the inventory according to the name N |  
|  4     | System displays a list of items containing N in their name |
|  5 (Optional)| Shop assistant goes to the page of a specific item to see its specific information | 

### Use case 6, UC6 Creating a new Employee Account
| Actors Involved        |  Administrator |
| ------------- |:-------------:| 
|  Precondition     | Employee Account does not exist |  
|  Post condition     | Employee Account exists |
|  Nominal Scenario     | The Administrator of the system selects into the application the creation of a new Employee account; the Administrator inserts all of the Employee’s personal data; the Administrator sets the new Employee’s role and privileges; The Administrator confirm the insertion |
|  Variants     | - |

### Use case 7, UC7 Deleting an existing Employee account
| Actors Involved        |  Administrator |
| ------------- |:-------------:| 
|  Precondition     | Employee Account exists |  
|  Post condition     | Employee Account does not exist |
|  Nominal Scenario     | The Administrator of the system search the Employee profile by ID; once found the Administrator presses the key to delete the account; the Administrator confirms the deletion |
|  Variants     | Account could be temporarily disabled |

### Use case 8, UC8 - Modifying an existing Employee account
| Actors Involved        |  Administrator |
| ------------- |:-------------:| 
|  Precondition     | Employee Account exists |  
|  Post condition     | Employee Account exists but modified |
|  Nominal Scenario     | The Administrator of the system search the Employee profile by ID; once found the Administrator presses the key to modify the selected account; the Administrator proceeds to modify the data; the Administrator saves the new account, confirming the update |
|  Variants     | It could be to modify the role / privileges / personal data of the user |


### Use case 9, UC9 - Add new fidelity card
| Actors Involved        | Customer's fidelity card && (Cashier OR Shop assistant) |
| ------------- |:-------------:| 
|  Precondition     | Customer U doesn't have a fidelity card |
| | Fidelity card N° does not exists|  
|  Post condition     | Fidelity card added in the system |
|	| Customer data linked to it |
|  Nominal Scenario     | Shop assistant / Cashier adds new fidelity card to the system. Card will obtain an incremental ID and will be linked with customer data. |
|  Variants     | If customer is under 13, creation fails |

##### Scenario 9.1 

| Scenario 9.1 | Correct creation |
| ------------- |:-------------:| 
|  Precondition     | Customer U doesn't have a fidelity card |
| | Fidelity card N° does not exists|  
|  Post condition     | Fidelity card added in the system |
|	| Customer data linked to it |
| Step#        | Description  |
|  1     | Shop Assistant (SA) / Cashier (C) press the "Add Fidelity Card" button |  
|  2     | SA / C fills form with customer data |
|  3     | System verifies that customer data satisfy constraints |
|  4     | System computes a valid fidelity card number |
|  5     | System links fidelity card number with customer data and stores them |
|  6     | System issues new card number and barcode that are sent to customer email |


##### Scenario 9.2

| Scenario 9.2 | Customer too young |
| ------------- |:-------------:| 
|  Precondition     | Customer U doesn't have a fidelity card |
| | Fidelity card N° does not exists|  
|  Post condition     | Fidelity card not added in the system |
| Step#        | Description  |
|  1     | Shop Assistant (SA) / Cashier (C) press the "Add Fidelity Card" button |  
|  2     | SA / C fills form with customer data |
|  3     | System verifies that customer data satisfy constraints |
|  4     | System aborts creation because customer is too young |


### Use case 10, UC10 - Disable fidelity card
| Actors Involved        | Customer's fidelity card && (Cashier OR Shop assistant) |
| ------------- |:-------------:| 
|  Precondition     | Fidelity Card exists |  
|  Post condition     | Fidelity Card disabled |
|  Nominal Scenario     | Cashier/Shop Assistant disables Fidality Card after customer's request |
|  Variants     | - |

##### Scenario 10.1 

| Scenario 10.1 | Correct disabling |
| ------------- |:-------------:| 
|  Precondition     | Fidelity Card exists |  
|  Post condition     | Fidelity Card disabled |
| Step#        | Description  |
|  1     | Shop Assistant (SA) / Cashier (C) press the "Disable Fidelity Card" button |  
|  2     | System Asks confirmation |
|  3     | System disables fidelity card |
|  4     | System informs customer by email |

### Use case 11, UC11 - Find card balance
| Actors Involved        | Customer's fidelity card && (Cashier OR Shop assistant) |
| ------------- |:-------------:| 
|  Precondition     | Fidelity Card exists |  
|  Post condition     | Fidelity Card Balance retrieved |
|  Nominal Scenario     | Cashier reads card balance at transaction time (after scanning the card); If balance above a certain threshold, a % discount is applied |
|  Variants     | Shop Assistant retrieves card balance after customer's request by card number |
|  Variants     | Shop Assistant retrieves card balance after customer's request by customer name and surname |

##### Scenario 11.1 

| Scenario 11.1 | Cash desk scenario |
| ------------- |:-------------:| 
|  Precondition     | Fidelity Card exists |  
|  Post condition     | Fidelity Card Balance retreived |
| Step#        | Description  |
|  1     | Cash desk module asks System to retrieve balance for a certain Fidelity Card number |  
|  2     | System returns balance |
|  3     | Cashier communicate balance to customer |

##### Scenario 11.2

| Scenario 11.2 | Shop assistant scenario |
| ------------- |:-------------:| 
|  Precondition     | Fidelity Card exists |  
|  Post condition     | Fidelity Card Balance retreived |
| Step#        | Description  |
|  1     | Shop assistant asks System to retrieve Fidelity card balance for a certain Customer name and surname |  
|  2     | System returns balance |
|  3     | Shop assistant communicate balance to customer |


### Use case 12, UC12 - Perform Analysis
| Actors Involved        | EZShop's sales manager |
| ------------- |:-------------:| 
|  Precondition     | There has been more than one transaction |  
|  Post condition     | Analysis produced |
|       | Trend produced |
|       | Graphs produced |
|  Nominal Scenario     | Sales manager enter in his panel and checks graphs/trends on the last months for all the products|
|  Variants     | Sales manager ask EZShop's SW to produce yearly/weekly reports for all the products |
|  Variants     | Sales manager ask EZShop's SW to produce reports on a certain category of product |
|  Variants     | Sales manager ask EZShop's SW to produce reports on a specific product |

##### Scenario 12.1 

| Scenario 12.1 | Monthly analysis for all the products |
| ------------- |:-------------:| 
|  Precondition     | There has been more than one transaction during this month |  
|  Post condition     | Analysis produced |
|       | Trend produced |
|       | Graphs produced |
| Step#        | Description  |
|  1     | Sales Manager (SM) access his panel |  
|  2     | SM reaches analysis page |
|  3  | SM set filters: monthly analysis for all products |
|  4  | SM press "generate" button |
|  5  | System produces graphs/trends/analysis |

##### Scenario 12.2

| Scenario 12.2 | Weekly analysis for a specific product |
| ------------- |:-------------:| 
|  Precondition     | - |  
|  Post condition     | Analysis produced |
|       | Trend produced |
|       | Graphs produced |
| Step#        | Description  |
|  1     | Sales Manager (SM) access his panel |  
|  2     | SM reaches analysis page |
|  3  | SM set filters: weekly analysis for a particular product |
|  4  | SM press "generate" button |
|  5  | System produces graphs/trends/analysis |

### Use case 13, UC13 Managing accounting
| Actors Involved        |  Administrator / Sales Manager |
| ------------- |:-------------:| 
|  Precondition     | There has been at least one transaction |  
|  Post condition     | - |
|  Nominal Scenario     | The Administrator of the system opens the accounting section; he can now view all the transaction registered in the app and select something; after making a selection he can extract the data by printing a report; 
|  Variants     | It can be used to filter by different categories such as the sold items or purchased stocks or also filter based on the date, value etc |

##### Scenario 13.1
| Scenario 1 | Consulting grand total of expenses and revenues |
| ------------- |:-------------:| 
|  Precondition     | There has been at least one transaction |  
|  Post condition     | - |
| Step#        | Description  |
|  1     | The Administrator  selects to see the total of revenues vs expenses |  
|  2     | He sets the parameters to see data of the whole year |
|  3     | He sets the parameters to see data of every section (employees'salaries, sales, suppliers' orders) |
|  4     | The page with the selected informations loads |
|  5 (optional)     | The page can be exported in .pdf or .xls |

##### Scenario 13.2
| Scenario 2 | Consulting total revenue from sales in the last month |
| ------------- |:-------------:| 
|  Precondition     | There has been at least one transaction |  
|  Post condition     | - |
| Step#        | Description  |
|  1     | The Administrator  selects to see the total of revenues vs expenses |  
|  2     | He sets the parameters to see data from the last month |
|  3     | He sets the parameters to see only data from sales |
|  4     | The page with the selected informations loads |
|  5 (optional)     | The page can be exported in .pdf or .xls |

##### Scenario 13.3
| Scenario 3 | Consulting total expenses in suppliers'orders in the last 6 months |
| ------------- |:-------------:| 
|  Precondition     | There has been at least one transaction |  
|  Post condition     | - |
| Step#        | Description  |
|  1     | The Administrator  selects to see the total of revenues vs expenses |  
|  2     | He sets the parameters to see data from the last 6 month |
|  3     | He sets the parameters to see only data from the supplier section |
|  4     | The page with the selected informations loads |
|  5 (optional)     | The page can be exported in .pdf or .xls |


# Glossary



![Alt text](Glossary_Diagram.png?raw=true "Glossary Diagram")



# System Design


![Alt text](SystemDesign_Diagram.png?raw=true "System Design Diagram")



# Deployment Diagram 

![Alt text](DeploymentDiagram_Diagram.png?raw=true "Deployment Diagram")



