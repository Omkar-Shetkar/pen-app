import client from "./client";

const login = (credentials) => {
  console.log("Logging in for ", credentials.username);
  return client.post("/auth/login", credentials);
};

export default {
  login,
};
