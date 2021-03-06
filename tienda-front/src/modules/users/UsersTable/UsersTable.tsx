import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableFooter,
  TableHead,
  TablePagination,
  TableRow,
} from "@mui/material";

import { User } from "../types";
import UserRow from "./UserRow";

import { useTypeSafeTranslation } from "#root/lib/hooks/useTypeSafeTranslation";

type UsersTableProps = {
  data: User[];
  onDelete?: (user: User) => void;
  onEdit?: (user: User) => void;

  rowsPerPage: number;
  page: number;
  setPage: (val: number) => void;
  setRowsPerPage: (val: number) => void;
};

const UsersTable = ({
  data,
  onDelete,
  onEdit,
  rowsPerPage,
  page,
  setPage,
  setRowsPerPage,
}: UsersTableProps) => {
  const { t } = useTypeSafeTranslation();

  return (
    <TableContainer component={Paper}>
      <Table aria-label="products table">
        <TableHead>
          <TableRow>
            <TableCell />
            <TableCell>{t("common.username")}</TableCell>
            <TableCell>{t("common.name")}</TableCell>
            <TableCell>{t("pages.user.role")}</TableCell>
            <TableCell align="right">{t("pages.user.active")}</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.map(row => (
            <UserRow key={row?.name} row={row} onDelete={onDelete} onEdit={onEdit} />
          ))}
        </TableBody>
        <TableFooter>
          <TableRow>
            <TablePagination
              rowsPerPageOptions={[5, 10, 25]}
              colSpan={3}
              count={data.length}
              rowsPerPage={rowsPerPage}
              page={page}
              SelectProps={{ inputProps: { "aria-label": "rows per page" }, native: true }}
              onPageChange={(evt, newPage) => setPage(newPage)}
              onRowsPerPageChange={evt => {
                setRowsPerPage(parseInt(evt.target.value, 10));
                setPage(0);
              }}
              labelRowsPerPage={t("common.rowsPerPage")}
            />
          </TableRow>
        </TableFooter>
      </Table>
    </TableContainer>
  );
};

export default UsersTable;
