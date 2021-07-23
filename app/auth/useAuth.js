import jwtDecode from "jwt-decode";
import { useContext } from "react";
import AuthContext from "./context";
import authStorage from "./storage";
import userApi from "../api/user";
import userType from "../config/userType";

export default useAuth = () => {
  const { user, setUser, setIsScribe } = useContext(AuthContext);

  const login = async (authToken) => {
    const user = jwtDecode(authToken);
    setUser(user);
    authStorage.storeToken(authToken);
    const userDetails = await userApi.getUserDetails(user.sub);
    console.log("Setting user type to ", userDetails.data.userType);
    setIsScribe(userDetails.data.userType === userType.SCRIBE ? true : false);
  };

  const logout = () => {
    setUser(null);
    setIsScribe(false);
    authStorage.removeToken();
  };

  return { login, logout, user };
};
