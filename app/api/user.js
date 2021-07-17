import apiClient from "./client";

const endPoint = "/users";
const getUserDetails = (userId) => apiClient.get(endPoint + "/" + userId);

export default {
  getUserDetails,
};
