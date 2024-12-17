db.createUser({
  user: "myuser",
  pwd: "password",
  roles: [{ role: "readWrite", db: "mydatabase" }]
});

db.client.insertMany([
  { name: "John Doe", email: "john.doe@example.com", age: 30 },
  { name: "Jane Doe", email: "jane.doe@example.com", age: 25 },
  { name: "Alice Smith", email: "alice.smith@example.com", age: 28 }
]);