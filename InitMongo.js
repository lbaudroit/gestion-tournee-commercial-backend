db.createUser({
  user: "myuser",
  pwd: "secret",
  roles: [{ role: "readWrite", db: "mydatabase" }]
});

