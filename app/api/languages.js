import apiClient from "./client";

const endPoint = "/languages";
const getLanguages = () => apiClient.get(endPoint);

export default {
  getLanguages,
};
