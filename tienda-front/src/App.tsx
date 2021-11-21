import { Routes, Route } from "react-router-dom";

import LoginPage from "./modules/auth/LoginPage";
import RequireAuth from "./modules/auth/RequireAuth";
import CategoriesPage from "./modules/categories/CategoriesPage";
import NotFoundPage from "./modules/errors/NotFoundPage";
import HomePage from "./modules/home/HomePage";
import ProductsPage from "./modules/products/ProductsPage";
import UsersPage from "./modules/users/UsersPage";

const App = () => {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/home" element={<HomePage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route
        path="/categories"
        element={
          <RequireAuth>
            <CategoriesPage />
          </RequireAuth>
        }
      />
      <Route
        path="/products"
        element={
          <RequireAuth>
            <ProductsPage />
          </RequireAuth>
        }
      />
      <Route
        path="/users"
        element={
          <RequireAuth>
            <UsersPage />
          </RequireAuth>
        }
      />

      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
};

export default App;
