import apiClient from "./client";

const endPoint = "/users";
const getUserDetails = (userId) => apiClient.get(endPoint + "/" + userId);
const updateUserDetails = (userId, userDetails) =>
  apiClient.post(endPoint + "/" + userId, userDetails);

export default {
  getUserDetails,
  updateUserDetails,
};
