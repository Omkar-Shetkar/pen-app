import client from "./client";

const login = (credentials) => {
  console.log("Logging in for ", credentials.username);
  return client.post("/auth/login", credentials);
};

const register = (user) => {
  console.log("Registering ", user.firstName);
  return client.post("/auth/register", user);
};

export default {
  login,
  register,
};
