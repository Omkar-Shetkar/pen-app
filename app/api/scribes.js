import apiClient from "./client";

const endPoint = "/scribes";

const find = (searchCriteria, language) => {
  const from = new Date(searchCriteria.examDate);

  console.log("search criteria: ", searchCriteria);

  const fromDate = new Date(
    Date.UTC(
      from.getFullYear(),
      from.getMonth(),
      from.getDate(),
      from.getHours(),
      from.getMinutes()
    )
  );
  const toDate = new Date(
    Date.UTC(
      from.getFullYear(),
      from.getMonth(),
      from.getDate(),
      from.getHours() + parseInt(searchCriteria.durationHours),
      from.getMinutes() + parseInt(searchCriteria.durationMinutes)
    )
  );

  return apiClient.get(endPoint, {
    from: fromDate.toISOString(),
    to: toDate.toISOString(),
    language: searchCriteria.language,
  });
};

export default {
  find,
};
