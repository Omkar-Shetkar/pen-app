import apiClient from "./client";

const endPoint = "/requests";

const createRequest = (requestUpdate) => {
  const data = new FormData();
  data.append("scribeId", requestUpdate.scribeId);
  data.append("candidateId", requestUpdate.candidateId);
  data.append("startTime", requestUpdate.startTime);
  data.append("endTime", requestUpdate.endTime);
  data.append("language", requestUpdate.language);
  data.append("address", requestUpdate.address);
  data.append("exam", requestUpdate.exam);

  console.log("Create Request API called...", requestUpdate);
  return apiClient.post(endPoint, requestUpdate);
};

const findRequests = (requestSearchFilter) => {
  console.log("Find Request API called...", requestSearchFilter);
  return apiClient.get(endPoint, requestSearchFilter);
};

const updateRequest = (id, requestUpdate) => {
  console.log("Request update API called...", id, requestUpdate);
  return apiClient.patch(endPoint + "/" + id, requestUpdate);
};

const deleteRequest = (id) => {
  console.log("Delete Request API called...", id);
  return apiClient.delete(endPoint + "/" + id);
};

export default {
  createRequest,
  findRequests,
  updateRequest,
  deleteRequest,
};
