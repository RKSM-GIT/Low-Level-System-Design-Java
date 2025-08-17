# Low-Level System Design in Java

A collection of well-designed, scalable Java projects demonstrating various system design principles, design patterns, and best practices.

## 🚗 Parking Lot System

A robust, thread-safe parking lot management system with flexible allocation and pricing strategies.

### 🎯 Features

- **Thread-safe operations** with synchronized methods
- **O(log n) spot allocation** using PriorityQueue
- **Flexible pricing strategies** (hourly, daily, etc.)
- **Multiple allocation strategies** (nearest size, first available, etc.)
- **Support for multiple vehicle types** (Motorcycle, Car, Truck)
- **Multi-floor parking support**
- **Distance-based spot prioritization**
- **Singleton ticket service** for centralized ticket management

### 🏗️ Architecture Overview

```
ParkingLotService (Main Controller)
├── ParkingFloor (Floor Management)
│   └── ParkingSpot (Individual Spots)
├── TicketService (Singleton)
│   ├── Ticket (Entry)
│   └── Receipt (Exit)
├── Strategy Pattern
│   ├── AllocationStrategy
│   └── CostStrategy
└── Vehicle Hierarchy
    ├── Motorcycle
    ├── Car
    └── Truck
```

### 🚀 How to Use ParkingLotService

#### Basic Setup

```java
// 1. Create parking spots
List<ParkingSpot> spots = Arrays.asList(
    new ParkingSpot("C1", 1, SpotType.MEDIUM, 1.0),
    new ParkingSpot("M1", 1, SpotType.SMALL, 2.0),
    new ParkingSpot("T1", 1, SpotType.LARGE, 3.0)
);

// 2. Create parking floor
ParkingFloor floor = new ParkingFloor(1, spots);

// 3. Initialize parking lot service
ParkingLotService parkingLot = new ParkingLotService(Arrays.asList(floor));

// 4. Set strategies
parkingLot.setAllocationStrategy(new NearestSizeStrategy());
parkingLot.setCostStrategy(new HourlyCostStrategy());
```

#### Vehicle Operations

```java
// Park a vehicle
Vehicle car = new Car("KA-01-1234");
Ticket ticket = parkingLot.parkVehicle(car);

// Unpark a vehicle
Receipt receipt = parkingLot.unparkVehicle(ticket);
System.out.println("Amount: " + receipt.getAmount());
```

#### Strategy Switching

```java
// Switch to different allocation strategy
parkingLot.setAllocationStrategy(new FirstAvailableStrategy());

// Switch to different pricing strategy
parkingLot.setCostStrategy(new DailyCostStrategy());
```

### 🎨 Design Principles Implemented

#### SOLID Principles

1. **Single Responsibility Principle (SRP)**
   - `ParkingLotService`: Manages parking operations
   - `TicketService`: Handles ticket lifecycle
   - `ParkingFloor`: Manages floor-level operations
   - `ParkingSpot`: Represents individual spot state

2. **Open/Closed Principle (OCP)**
   - New allocation strategies can be added without modifying existing code
   - New pricing strategies can be implemented independently
   - New vehicle types can be added by extending the Vehicle class

3. **Liskov Substitution Principle (LSP)**
   - All vehicle subclasses (Car, Motorcycle, Truck) can be used interchangeably
   - All strategy implementations can be swapped seamlessly

4. **Interface Segregation Principle (ISP)**
   - `AllocationStrategy` and `CostStrategy` are focused, single-purpose interfaces
   - No client is forced to depend on methods it doesn't use

5. **Dependency Inversion Principle (DIP)**
   - `ParkingLotService` depends on abstractions (`AllocationStrategy`, `CostStrategy`)
   - High-level modules don't depend on low-level modules

#### Design Patterns

1. **Strategy Pattern**
   - Allocation strategies: `NearestSizeStrategy`, `FirstAvailableStrategy`
   - Cost strategies: `HourlyCostStrategy`, `DailyCostStrategy`

2. **Singleton Pattern**
   - `TicketService` ensures single instance for centralized ticket management

3. **Factory Pattern** (implicit)
   - Vehicle creation through constructors
   - Ticket creation through `TicketService`

4. **Template Method Pattern** (in allocation logic)
   - Common allocation flow with customizable spot selection

### 🔒 Thread Safety Features

- **Synchronized methods** in `ParkingLotService.parkVehicle()` and `unparkVehicle()`
- **Synchronized spot assignment** in `ParkingFloor.tryAssignSpot()` and `unassignSpot()`
- **ConcurrentHashMap** in `TicketService` for thread-safe ticket storage
- **Thread-safe PriorityQueue** operations for spot allocation

### ⚡ Performance Optimizations

- **O(log n) spot allocation** using PriorityQueue based on distance
- **EnumMap** for O(1) spot type lookups
- **HashMap** for O(1) occupied spot lookups
- **Efficient preference-based allocation** with early termination

### 🔧 Extensibility Features

#### Adding New Allocation Strategies

```java
public class PremiumSpotStrategy implements AllocationStrategy {
    @Override
    public ParkingSpot allocate(List<ParkingFloor> floors, Vehicle vehicle) {
        // Premium allocation logic
        return premiumSpot;
    }
}
```

#### Adding New Pricing Strategies

```java
public class PeakHourStrategy implements CostStrategy {
    @Override
    public long calculateCost(Ticket ticket, Instant exitTime) {
        // Peak hour pricing logic
        return calculatedCost;
    }
}
```

#### Adding New Vehicle Types

```java
public class ElectricCar extends Vehicle {
    public ElectricCar(String license) {
        super(license, VehicleType.ELECTRIC_CAR);
    }
}
```

### 📊 System Capabilities

- **Multi-floor support**: Unlimited floors with independent management
- **Vehicle type flexibility**: Easy addition of new vehicle types
- **Dynamic pricing**: Runtime strategy switching
- **Scalability**: Efficient data structures for large parking lots
- **Concurrency**: Thread-safe operations for high-traffic scenarios

### 🧪 Testing Considerations

- Unit tests for each strategy implementation
- Integration tests for parking/unparking flows
- Concurrency tests for thread safety
- Performance tests for allocation algorithms
- Edge case testing (full parking lot, invalid tickets, etc.)

---

## 📚 Future Projects

This repository will be expanded with additional low-level system design projects:

- [ ] **Notification System** - Event-driven notification architecture
- [ ] **E-commerce Order Management** - Microservices-based order processing
- [ ] **Social Media Feed** - Real-time feed generation system
- [ ] **Ride-Sharing System** - Location-based ride matching
- [ ] **Hotel Booking System** - Reservation and inventory management
- [ ] **File Storage System** - Distributed file storage with replication

Each project will demonstrate different aspects of system design, from data structures and algorithms to distributed systems and scalability patterns.

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for:

- Bug fixes
- Performance improvements
- Additional features
- Documentation enhancements
- New system design projects

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.