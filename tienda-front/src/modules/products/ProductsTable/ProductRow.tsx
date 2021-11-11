import { Delete, Edit, KeyboardArrowDown, KeyboardArrowUp } from "@mui/icons-material";
import { Box, Collapse, Stack, TableCell, TableRow, Typography } from "@mui/material";
import { Fragment, useState } from "react";

import IconButton from "#root/components/IconButton";
import { IMAGE_FALLBACK_URL } from "#root/lib/constants";
import { Product } from "#root/modules/products/types";

type ProductRowProps = {
  onDelete?: (id: number) => void;
  onEdit?: (product: Product) => void;
  row: Product;
};

const ProductRow = ({ onDelete, onEdit, row }: ProductRowProps) => {
  const [isExpanded, setIsExpanded] = useState(false);

  return (
    <Fragment>
      <TableRow sx={{ "& > *": { borderBottom: "unset" } }}>
        <TableCell width="4rem">
          <IconButton
            aria-label="expand row"
            tip="More details"
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
                  {`Product: ${row.code}-${row.name}`}
                </Typography>
                <Stack direction="row" spacing={2} alignItems="center">
                  <IconButton
                    aria-label="update"
                    tip="Update"
                    iconButtonProps={{ color: "info" }}
                    onClick={() => onEdit?.(row)}
                  >
                    <Edit />
                  </IconButton>
                  <IconButton
                    aria-label="delete"
                    tip="Delete"
                    iconButtonProps={{ color: "error" }}
                    onClick={() => onDelete?.(row.idProduct)}
                  >
                    <Delete />
                  </IconButton>
                </Stack>
              </Stack>

              <Stack direction="row" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography component="p" marginY="1rem">
                    {row.description}
                  </Typography>

                  <Typography component="p">Quantity: {row.quantity}</Typography>
                  <Typography component="p">Unit Price: {row.unitPrice}</Typography>
                </Box>
                <Box height="10rem">
                  <img src={row.pictures || IMAGE_FALLBACK_URL} alt="product" height="100%" />
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