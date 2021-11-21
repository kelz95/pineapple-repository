import { Delete, Edit, KeyboardArrowDown, KeyboardArrowUp } from "@mui/icons-material";
import { Box, Collapse, Stack, TableCell, TableRow, Typography } from "@mui/material";
import { Fragment, useState } from "react";
import { useTranslation } from "react-i18next";

import IconButton from "#root/components/IconButton";
import { IMAGE_FALLBACK_URL } from "#root/lib/constants";
import { namespaces } from "#root/translations/i18n.constants";

import { Product } from "../types";

type ProductRowProps = {
  onDelete?: (product: Product) => void;
  onEdit?: (product: Product) => void;
  onEditImage?: (product: Product) => void;
  row: Product;
};

const ProductRow = ({ onDelete, onEdit, onEditImage, row }: ProductRowProps) => {
  const [isExpanded, setIsExpanded] = useState(false);

  const { t } = useTranslation(namespaces.translation);

  return (
    <Fragment>
      <TableRow sx={{ "& > *": { borderBottom: "unset" } }}>
        <TableCell width="4rem">
          <IconButton
            aria-label="expand row"
            tip={t("productRow.details")}
            onClick={() => setIsExpanded(!isExpanded)}
          >
            {isExpanded ? <KeyboardArrowUp /> : <KeyboardArrowDown />}
          </IconButton>
        </TableCell>

        <TableCell component="th" scope="row">
          {row.code}
        </TableCell>

        <TableCell>{row.name}</TableCell>
        <TableCell align="right">{row.quantity}</TableCell>
        <TableCell align="right">{row.unitPrice}</TableCell>
      </TableRow>
      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
          <Collapse in={isExpanded} timeout="auto" unmountOnExit>
            <Box sx={{ marginY: 2, marginX: 1 }}>
              <Stack direction="row" alignItems="center" justifyContent="space-between">
                <Typography component="h5" variant="h6" gutterBottom>
                  {`${t("productRow.product")}: ${row.code}-${row.name}`}
                </Typography>
                <Stack direction="row" spacing={2} alignItems="center">
                  <IconButton
                    aria-label="update"
                    tip={t("productRow.bUpdate")}
                    iconButtonProps={{ color: "info" }}
                    onClick={() => onEdit?.(row)}
                  >
                    <Edit />
                  </IconButton>
                  <IconButton
                    aria-label="delete"
                    tip={t("productRow.bDelete")}
                    iconButtonProps={{ color: "error" }}
                    onClick={() => onDelete?.(row)}
                  >
                    <Delete />
                  </IconButton>
                </Stack>
              </Stack>

              <Stack direction="row" alignItems="center" justifyContent="space-between">
                <Box sx={{ borderRadius: 2, padding: 2, width: "45%" }}>
                  <Typography component="p" marginY="1rem">
                    {t("productRow.description")}: {row.description}
                  </Typography>

                  <Typography component="p">
                    {t("productRow.quantity")}: {row.quantity}
                  </Typography>
                  <Typography component="p">
                    {t("productRow.uPrice")}: {row.unitPrice}
                  </Typography>
                </Box>
                <Box sx={{ borderRadius: 2, padding: 2 }}>
                  <Typography component="h6" marginBottom="1rem" variant="h6">
                    {t("productRow.category")}
                  </Typography>

                  <Typography component="p">
                    {t("productRow.code")}: {row.productCategory.code}
                  </Typography>
                  <Typography component="p">
                    {t("productRow.description")}: {row.productCategory.description}
                  </Typography>
                </Box>
                <Box
                  height="10rem"
                  onClick={() => onEditImage?.(row)}
                  sx={{ ":hover": { cursor: "pointer", opacity: 0.5 } }}
                >
                  <img src={row.picture || IMAGE_FALLBACK_URL} alt="product" height="100%" />
                </Box>
              </Stack>
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </Fragment>
  );
};
export default ProductRow;
