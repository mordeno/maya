# Simple Wallet with Send Money

This is a simple wallet application that allows users to send money to each other.

## Features
- Check wallet balance
- Show list of contacts
- Send money to a contact
- View transaction history
- View transaction details

## Send Money Simulation (Successful Transfer)
1. User selects a valid contact from the contact list.
2. User creates a transer request to the selected contact.
3. The system checks if the user has sufficient balance or is within the daily limit to complete the transfer.
4. If transfer is valid, the system creates a new transaction record.
5. The system deducts sender's available balance and adds to sender's on hold balance.
6. A double entry is created for the journal: 
    - Debit: Sender's available balance
    - Credit: Sender's on hold balance
7. A new transaction event is created with type 'CREATED'
8. The system simulates successful transfer.
9. Receiver's available balance is updated with the transferred amount.
10. Upon successful transfer, the system updates the transaction status to "Completed" and updates the journal entries:
    - Debit: Sender's on hold balance
    - Credit: Receiver's available balance
11. A new transaction event is created with type 'COMPLETED'.

## How to Run 
1. Clone the repository and navigate to the project directory.
2. Run `docker-compose up -d --build` to start the application.
3. Or you can run the application using the following command:
```bash
./mvnw spring-boot:run
```
3. The application will be available at http://localhost:8080.

## API Endpoints
All endpionts must have a `X-User-Id` header to identify the user.
### 1. Get Account Balance
Send a `GET` request to the following endpoint:
```
GET /balance
```
Example Response:
```json
{
  "accountId": 5,
  "balance": "20000.00",
  "dailyLimit": "10000.00",
  "currency": "PHP"
}
```
### 2. Get Contacts
Send a `GET` request to the following endpoint:
```
GET /contacts
```
Example Response:
```json
[
  {
    "userId": 1,
    "name": "Leanne Graham",
    "email": "Sincere@april.biz"
  },
  {
    "userId": 2,
    "name": "Ervin Howell",
    "email": "Shanna@melissa.tv"
  },
  {
    "userId": 5,
    "name": "Chelsey Dietrich",
    "email": "Lucio_Hettinger@annie.ca"
  }
]
```
### 3. Get Transaction History
Send a `GET` request to the following endpoint:
```
GET /transactions
```
Example Response:
```json
[
  {
    "transactionId": 3,
    "name": "Leanne Graham",
    "amount": "20000.00",
    "type": "SENT",
    "date": "2026-05-17T20:28:38.302724",
    "currency": "PHP"
  },
  {
    "transactionId": 2,
    "name": "Leanne Graham",
    "amount": "30000.00",
    "type": "SENT",
    "date": "2026-05-17T20:27:58.804228",
    "currency": "PHP"
  },
  {
    "transactionId": 1,
    "name": "Leanne Graham",
    "amount": "30000.00",
    "type": "SENT",
    "date": "2026-05-17T20:02:51.988222",
    "currency": "PHP"
  }
]
```
### 4. Get Transaction Details
Send a `GET` request to the following endpoint:
```
GET /transactions/{transactionId}
```
Example Response:
```json
{
  "transactionId": 1,
  "senderName": "Chelsey Dietrich",
  "receiverName": "Leanne Graham",
  "amount": "20000.00",
  "type": "RECEIVED",
  "status": "COMPLETED",
  "date": "2026-05-17T21:06:05.630779",
  "currency": "PHP"
}
```
### 5. Send Money
Send a `POST` request to the following endpoint:
```
POST /transfer
```
Example Request:
```
curl -X POST "http://localhost:8080/transfer" 
     -H "Content-Type: application/json" 
     -H "X-User-Id: 5"
     -H "Idempotency-Key" "123"
     -d '{"recipientId": 1, "amount": 20000.00}'
```
Example Response:
```json
{
  "transactionId": 3,
  "senderName": "Patricia Lebsack",
  "receiverName": "Leanne Graham",
  "amount": "20000.00",
  "type": "SENT",
  "status": "COMPLETED",
  "date": "2026-05-17T21:14:11.077977673",
  "currency": "PHP"
}
```
## Tables
`users`, `accounts`, and `contacts` tables are populated at start-up using data from `/resources/data.sql`
- `accounts` - stores user account information including balance and daily limit.
- `transactions` - stores transaction records including sender, receiver, amount, type, and status.
- `journalEntries` - stores journal entries for double-entry bookkeeping.
- `transactionEvents` - stores events related to transactions for auditing and tracking purposes.
- `users` - stores user information including id and name.
- `contacts` - stores user contacts for easy lookup when sending money.

