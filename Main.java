import java.util.Scanner;

// Book class to represent library books
class Book {
    int id;
    String title;
    String author;
    boolean isAvailable;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    @Override
    public String toString() {
        //To print books that are store within the library
        return "Book{id=" + id + ", title='" + title + "', author='" + author + "', available=" + isAvailable + "}";
    }
}

// Node class for Binary Search Tree
class BSTNode {
    Book book;
    //Create a BST for left and right
    BSTNode left, right;

    public BSTNode(Book book) {
        //Assigns the input book to the book variable
        this.book = book;
        //Sets both left and right to null because it doesn't has children yet.
        left = right = null;
    }
}

// Queue implementation for book reservations
class ReservationQueue {
    //QNode Class represents each node in queue
    class QNode {
        int bookId;
        String memberName;
        QNode next;

        //Constructor to create a new node
        public QNode(int bookId, String memberName) {
            this.bookId = bookId;
            this.memberName = memberName;
            this.next = null;
        }
    }
    //Track the front and rear of queue
    QNode front, rear;

    //Construction for Reservation Queue
    public ReservationQueue() {
        //Initialize an empty queue
        front = rear = null;
    }

    //Add new reservation to the queue
    public void enqueue(int bookId, String memberName) {
        //create a new node with the given book ID and member Name
        QNode newNode = new QNode(bookId, memberName);
        
        //If queue is empty, the new node becomes front and rear
        if (rear == null) {
            front = rear = newNode;
            return;
        }
        
        rear.next = newNode;
        rear = newNode;
    }

    //Remove reservatino from queue
    public QNode dequeue() {
        //if the queue is empty, return null
        if (front == null) return null;
        
        //store the front node to retun later
        QNode temp = front;
        //move the front pointer to the next node
        front = front.next;
        
        //if front becomes null, the queue is empty
        if (front == null) rear = null;
        
        //return the original front node
        return temp;
    }
}

// Library Management System class
class LibraryManagementSystem {
    private BSTNode root; //Root for Binary Search Tree
    private ReservationQueue reservations; //Queue for book reservation

    //Construct an empty BST and Reservation queue
    public LibraryManagementSystem() {
        root = null;
        reservations = new ReservationQueue();
    }

    // Insert a book into BST
    public void addBook(Book book) {
        root = insertRec(root, book);
    }

    //Recursive helper method to insert a book into the BST
    private BSTNode insertRec(BSTNode root, Book book) {
        //if tree is empty or reached a leaf, create a new node
        if (root == null) {
            root = new BSTNode(book);
            return root;
        }

        //else, recurse down the tree
        if (book.id < root.book.id)
            root.left = insertRec(root.left, book);
        else if (book.id > root.book.id)
            root.right = insertRec(root.right, book);

        return root;
    }

    // Search for a book in BST
    public Book searchBook(int id) {
        BSTNode node = searchRec(root, id);
        return node != null ? node.book : null;
    }

    //Recursive helper to search for book
    private BSTNode searchRec(BSTNode root, int id) {
        //base case: root is null or id is found
        if (root == null || root.book.id == id)
            return root;

        //recuse left is id is less than current node, else right
        if (root.book.id > id)
            return searchRec(root.left, id);

        return searchRec(root.right, id);
    }

    // Reserve a book
    public void reserveBook(int bookId, String memberName) {
        Book book = searchBook(bookId);
        //if book is not null and the book is not available create a reservation for book id and member name
        if (book != null && !book.isAvailable) {
            reservations.enqueue(bookId, memberName);
            System.out.println("Book reserved for " + memberName);
        } 
        //else if the book is not null and the book is available, print out no need to reserve
        else if (book != null && book.isAvailable) {
            System.out.println("Book is available, no need to reserve");
        }
        //else book is not found in the library
        else {
            System.out.println("Book not found!");
        }
    }

    // Process next reservation in the queue
    public void processNextReservation() {
        ReservationQueue.QNode reservation = reservations.dequeue();
        //if reservation is not null search for the book via book id
        if (reservation != null) {
            Book book = searchBook(reservation.bookId);
            //if book is not null and the book is not available, change the state of the book to available and print out reservation processed
            if (book != null && !book.isAvailable) {
                book.isAvailable = true;
                System.out.println("Reservation processed for " + reservation.memberName + " - Book: " + book.title);
            }
        } 
        //else print no reservation in queue
        else {
            System.out.println("No reservations in queue");
        }
    }

    // Borrow a book
    public void borrowBook(int id) {
        //search the book via book id
        Book book = searchBook(id);
        //if the book is not null and the book is available, change the state of the book into not available and print book borrowed
        if (book != null && book.isAvailable) {
            book.isAvailable = false;
            System.out.println("Book borrowed successfully: " + book.title);
        } 
        //else if the book is not null but is not available, print book is not available
        else if (book != null) {
            System.out.println("Book is not available: " + book.title);
        } 
        //else print book is not found in the library
        else {
            System.out.println("Book not found");
        }
    }

    // Return a book
    public void returnBook(int id) {
        //search book via book id
        Book book = searchBook(id);
        //if the book is not null and the book is not available, change the state of the book into available and print book returned
        if (book != null && !book.isAvailable) {
            book.isAvailable = true;
            System.out.println("Book returned successfully: " + book.title);
        } 
        //else if the book is null and the book is available, print book already in the library
        else if (book != null) {
            System.out.println("Book was already in library: " + book.title);
        } 
        //else the book is not found in the library
        else {
            System.out.println("Book not found");
        }
    }

    //Display all book
    public void displayAllBooks(){
        System.out.println("All books in the library:");
        inorderTraversal(root);
    }

    //recursive helper for inorder traversal of BST
    private void inorderTraversal(BSTNode node){
        if (node != null){
            inorderTraversal(node.left);
            System.out.println(node.book);
            inorderTraversal(node.right);
        }
    }

    //check if the library is empty
    public boolean isEmpty(){
        return root == null;
    }
}


// Main class to demonstrate the Library Management System
public class Main {
    public static void main(String[] args) {
        LibraryManagementSystem library = new LibraryManagementSystem();
        Scanner scan = new Scanner(System.in);
        int bookid = 1;

        //Display Menu Option
        while(true){
            System.out.println("\nWelcome to the Library!\nHow may I help you?\n1. Add book\n2. Borrow book\n3. Reserve book\n4. Return book\n5. Show all Books\n6. Book reservation process\n7. Exit");
            if (scan.hasNextInt()){
                int start = scan.nextInt();
                scan.nextLine();
    
                switch (start) {
                    case 1: //add book
                        while (true){
                            //prompt for book details
                            System.out.print("Please enter the book name: ");
                            String bookName = scan.nextLine();
                            System.out.print("Please enter the book author: ");
                            String bookAuthor = scan.nextLine();
                            
                            //add book to library
                            library.addBook(new Book(bookid, bookName, bookAuthor));
    
                            System.out.println("Successfully added book '" + bookName + "' by " + bookAuthor);
                            System.out.println("Would you like to add more books?\n1. Yes\n2. No");
                            int morebook = scan.nextInt();
                            scan.nextLine();
                            
                            //input validation
                            while(morebook != 1 && morebook != 2){
                                System.out.print("Invalid, please enter a valid number: ");
                                morebook = scan.nextInt();
                                scan.nextLine();
                            }
    
                            switch (morebook) {
                                case 1:
                                    bookid++;
                                    continue;
                                case 2:
                                    break;
                                default:
                                    
                            }
                            break;
                        }
                        break;
    
                    case 2: //borrow book
                        library.displayAllBooks();
                        System.out.print("Please enter the book ID you'd like to borrow: ");
                        int searchid = scan.nextInt();
                        scan.nextLine();
                        library.borrowBook(searchid);
                        break;
                    
                    case 3: //reserve book
                        if (library.isEmpty()){
                            System.out.println("The library is empty. No books available for reservation.");
                        } else{
                            library.displayAllBooks();
                            System.out.print("Please enter the book ID you'd like to reserve: ");
                            int reserveId = scan.nextInt();
                            scan.nextLine();
    
                            System.out.print("Please enter your name: ");
                            String memberName = scan.nextLine();
    
                            library.reserveBook(reserveId, memberName);
                        }
                        break;
    
                    case 4: //return book
                        if (library.isEmpty()){
                            System.out.println("The library is empty. No books available for return.");
                        } else{
                            library.displayAllBooks();
                            System.out.print("Please enter the ID of the book you'd like to return: ");
                            int returnId = scan.nextInt();
                            scan.nextLine();
                            library.returnBook(returnId);
                        }
                        break;
                        
                    case 5: //show all book
                        if (library.isEmpty()){
                            System.out.println("The library is empty. Would you like to add a book?\n1. Yes\n2. No");
                            int addBookChoice = scan.nextInt();
                            scan.nextLine();
                            if (addBookChoice == 1){
                                System.out.print("Please enter the book name: ");
                                String bookName = scan.nextLine();
                                System.out.print("Please enter the book author: ");
                                String bookAuthor = scan.nextLine();
                                library.addBook(new Book(bookid, bookName, bookAuthor));
                                System.out.println("Successfully added book '" + bookName + "' by " + bookAuthor);
                                bookid++;
                            } else {
                                System.out.println("I understand.");
                                break;
                            }
                        } else {
                            library.displayAllBooks();
                        }
                        break;
                    
                    case 6: //process reservation
                        library.processNextReservation();
                        break;

                    case 7: //Exit system
                        System.out.println("Thank you for using the Library");
                        scan.close();
                        return;
                        
                    default:
                        System.out.print("invalid, please enter a valid number: ");
                        scan.nextLine();
                }
            }  
        }
    }
}