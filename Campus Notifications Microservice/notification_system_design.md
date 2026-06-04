# Notification System Design

## Stage 1 – Notification System API Design

### Objective

The notification system should support:

* Placement Notifications
* Event Notifications
* Result Notifications
* Read/Unread Tracking
* Real-Time Delivery

### API Design

#### Create Notification

A secure API should allow authorized services such as HR, Placement Cell, or Examination Cell to create notifications for students.

Input information includes:

* User ID
* Notification Type
* Title
* Message
* Priority

Output:

* Notification ID
* Creation Status

#### Fetch Notifications

Students should be able to retrieve their notifications with pagination support to avoid loading large datasets at once.

Features:

* User-specific retrieval
* Pagination
* Sorting by latest notifications

#### Fetch Unread Notifications

Students should be able to retrieve only unread notifications for faster access to important updates.

#### Mark Notification as Read

A notification should be marked as read once the student views it.

#### Delete Notification

Users or administrators should be able to remove notifications when required.

### Security

All APIs should be protected using authentication and authorization mechanisms such as JWT tokens.

### Real-Time Notifications

Instead of requiring users to refresh the application repeatedly, the system should push notifications instantly using WebSockets or Server-Sent Events (SSE).

Flow:

Student Application → Notification Service → Real-Time Push → Student Device

---

# Stage 2 – Database Design

## Database Selection

### PostgreSQL

PostgreSQL is recommended because:

* Provides ACID-compliant transactions
* Supports advanced indexing
* Handles large-scale datasets efficiently
* Supports partitioning and replication
* Reliable for enterprise applications

### Notification Data Model

Each notification should contain:

* Notification ID
* User ID
* Notification Type
* Title
* Message
* Priority
* Read Status
* Creation Timestamp

### Typical Operations

#### Retrieve Unread Notifications

The system should quickly retrieve unread notifications for a specific user while displaying the most recent notifications first.

#### Mark Notification as Read

The system should update notification status once the user opens it.

### Scalability Challenges

Potential issues:

* Rapid growth of notification records
* Slow retrieval of unread notifications
* High database load during peak usage

### Solutions

* Database Indexing
* Table Partitioning
* Read Replicas
* Redis Caching

---

# Stage 3 – Query Optimization

## Problem Scenario

Consider:

* 50,000 students
* Millions of notifications

Every student may repeatedly access notifications throughout the day.

### Why Queries Become Slow

Without optimization:

* Large numbers of records must be scanned
* Additional sorting increases response time
* Database load increases significantly

### Optimization Strategy

Create a composite index based on:

* User Identifier
* Read Status
* Creation Time

### Benefits

* Faster user-specific filtering
* Efficient unread notification retrieval
* Reduced sorting overhead
* Improved overall performance

### Should Every Column Be Indexed?

No.

Excessive indexing causes:

* Additional storage consumption
* Slower insert operations
* Slower update operations
* Increased maintenance complexity

Indexes should only be created for frequently queried fields.

### Analytics Support

The system should support business queries such as:

* Students who received placement notifications recently
* Notification delivery trends
* Student engagement reports
* Placement campaign effectiveness

---

# Stage 4 – Performance Improvements

## Problem

Each page refresh triggering database queries can overload the system.

### Solution 1 – Redis Cache

Store frequently accessed notifications in Redis.

Advantages:

* Extremely fast access
* Reduced database load
* Improved user experience

Challenges:

* Cache invalidation
* Synchronization with database updates

### Solution 2 – Pagination

Instead of returning all notifications, retrieve notifications in smaller pages.

Advantages:

* Faster responses
* Lower memory usage
* Reduced network traffic

### Solution 3 – Read Replicas

Separate read and write operations.

Architecture:

* Primary Database → Writes
* Replica Databases → Reads

Advantages:

* Increased scalability
* Better throughput
* Reduced load on primary database

### Solution 4 – WebSocket Push

Rather than continuously polling the server, notifications should be pushed to connected users in real time.

Advantages:

* Immediate updates
* Fewer database requests
* Better user experience

---

# Stage 5 – Reliable Notify-All Design

## Problem With Basic Implementation

A simple sequential approach:

1. Send email
2. Save notification
3. Send push notification

creates several issues:

* Slow execution
* Poor scalability
* Difficult error recovery
* Partial failures

For example, notifying 50,000 students sequentially would take considerable time.

## Recommended Architecture

HR Portal

↓

Notification Service

↓

Message Queue (Kafka/RabbitMQ)

↓

Email Workers

↓

Push Notification Workers

↓

Database Workers

### Benefits

* Asynchronous processing
* High scalability
* Independent services
* Easy retries
* Better fault tolerance

### Reliable Processing Flow

1. Notification request is received.
2. Notification event is published to Kafka.
3. Worker services consume events.
4. Email and push notifications are processed independently.
5. Success and failure states are tracked.
6. Failed events are retried automatically.

### Handling Failures

If email delivery fails for some students:

* Retry automatically
* Record delivery status
* Move permanently failed events to Dead Letter Queue (DLQ)
* Allow reprocessing later

### Transaction Management

Database updates and external email delivery should not be part of a single transaction.

Recommended patterns:

* Event-Driven Architecture
* Transactional Outbox Pattern
* Retry Mechanisms
* Dead Letter Queues

---

# Stage 6 – Priority Inbox Design

## Objective

Students often receive many notifications.

The system should automatically prioritize and display the most important notifications.

### Priority Factors

Notification priority depends on:

1. Notification Type
2. Business Impact
3. Recency

### Weight Assignment

| Notification Type | Weight |
| ----------------- | ------ |
| Placement         | 10     |
| Result            | 7      |
| Event             | 5      |

### Priority Calculation

Priority Score should be calculated using:

Score =

(Type Weight × 0.5)

*

(Impact × 0.3)

*

(Recency × 0.2)

### Efficient Top-10 Retrieval

Instead of sorting every notification repeatedly, use a Min Heap (Priority Queue).

### Complexity

* Insertion: O(log K)
* Removal: O(log K)
* Top K Retrieval: O(N log K)

Where:

K = 10

### Advantages

* Efficient for large datasets
* No full sorting required
* Suitable for streaming notifications
* Scales well as notification volume grows

### Output

The system returns the top 10 highest-priority notifications sorted in descending order.

---

# High-Level Architecture

Clients

↓

API Gateway

↓

Notification Service

↓

PostgreSQL

Redis

Kafka

↓

Worker Services

↓

Email Service

Push Notification Service

SMS Service

---

# Final Recommendations

* PostgreSQL for persistent notification storage
* Redis for caching frequently accessed notifications
* Kafka for asynchronous processing and integration with email services
* WebSockets for real-time notification delivery
* Composite indexing for efficient unread notification retrieval
* Priority Queue (Min Heap) for Top-K notification ranking
* Retry and Dead Letter Queue mechanisms for reliability
* Event-Driven Architecture for scalability and fault tolerance

This design provides scalability, reliability, real-time delivery, efficient querying, and intelligent prioritization suitable for large-scale student notification systems.
