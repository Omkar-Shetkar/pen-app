import apiClient from "./client";

const endPoint = "/location";
const getCities = () => apiClient.get(endPoint + "/cities");

export default {
  getCities,
};
