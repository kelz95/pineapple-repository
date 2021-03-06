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

import { Product } from "#root/modules/products/types";
import { useTypeSafeTranslation } from "#root/lib/hooks/useTypeSafeTranslation";

import ProductRow from "./ProductRow";

type ProductsTableProps = {
  data: Product[];
  onDelete?: (product: Product) => void;
  onEdit?: (product: Product) => void;
  onEditImage?: (product: Product) => void;
  totalRows?: number;

  rowsPerPage: number;
  page: number;
  setPage: (val: number) => void;
  setRowsPerPage: (val: number) => void;
};

const ProductsTable = ({
  data,
  onDelete,
  onEdit,
  onEditImage,
  totalRows,
  rowsPerPage,
  page,
  setPage,
  setRowsPerPage,
}: ProductsTableProps) => {
  const { t } = useTypeSafeTranslation();

  return (
    <TableContainer component={Paper}>
      <Table aria-label="products table">
        <TableHead>
          <TableRow>
            <TableCell />
            <TableCell>{t("common.code")}</TableCell>
            <TableCell>{t("common.name")}</TableCell>
            <TableCell align="right">{t("pages.product.quantity")}</TableCell>
            <TableCell align="right">{t("pages.product.unitPrice")} (USD)</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.map(row => (
            <ProductRow
              key={row?.name}
              row={row}
              onDelete={onDelete}
              onEdit={onEdit}
              onEditImage={onEditImage}
            />
          ))}
        </TableBody>
        <TableFooter>
          <TableRow>
            <TablePagination
              rowsPerPageOptions={[5, 10, 25]}
              colSpan={3}
              count={totalRows || data.length}
              rowsPerPage={rowsPerPage}
              page={page}
              SelectProps={{ inputProps: { "aria-label": "rows per page" }, native: true }}
              onPageChange={(evt, newPage) => setPage(newPage)}
              onRowsPerPageChange={evt => {
                setRowsPerPage(parseInt(evt.target.value, 10));
                setPage(0);
              }}
              labelRowsPerPage={t("common.rowsPerPage")}
              // ActionsComponent={TablePaginationActions}
            />
          </TableRow>
        </TableFooter>
      </Table>
    </TableContainer>
  );
};

export default ProductsTable;
