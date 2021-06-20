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

  return apiClient.post(endPoint, requestUpdate);
};

export default {
  createRequest,
};
