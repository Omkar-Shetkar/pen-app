import { useState } from "react";
import strings from "../config/strings";
import useAuth from "../auth/useAuth";

export default useApi = (apiFunc) => {
  const [data, setData] = useState([]);
  const [error, setError] = useState(false);
  const [loading, setLoading] = useState(false);
  const { logout } = useAuth();

  const request = async (...args) => {
    setLoading(true);
    const response = await apiFunc(...args);
    setLoading(false);

    setError(!response.ok);
    setData(response.data);

    if (response.status === 401) {
      alert(strings.login.sessionExpired);
      logout();
      return;
    }

    return response;
  };

  return { data, error, loading, request };
};
