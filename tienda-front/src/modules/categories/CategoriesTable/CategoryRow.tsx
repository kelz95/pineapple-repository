import { Delete, Edit } from "@mui/icons-material";
import { Stack, TableCell, TableRow } from "@mui/material";

import IconButton from "#root/components/IconButton";
import { Category } from "../types";

import { useTypeSafeTranslation } from "#root/lib/hooks/useTypeSafeTranslation";

type CategoryRowProps = {
  onDelete?: (category: Category) => void;
  onEdit?: (category: Category) => void;
  row: Category;
};

const CategoryRow = ({ onDelete, onEdit, row }: CategoryRowProps) => {
  const { t } = useTypeSafeTranslation();

  return (
    <TableRow sx={{ "& > *": { borderBottom: "unset" } }}>
      <TableCell scope="row">{row.code}</TableCell>

      <TableCell>{row.description}</TableCell>

      <TableCell align="right">
        <Stack direction="row" spacing={2} alignItems="center">
          <IconButton
            aria-label="update"
            tip={t("common.update")}
            iconButtonProps={{ color: "info" }}
            onClick={() => onEdit?.(row)}
          >
            <Edit />
          </IconButton>
          <IconButton
            aria-label="delete"
            tip={t("common.delete")}
            iconButtonProps={{ color: "error" }}
            onClick={() => onDelete?.(row)}
          >
            <Delete />
          </IconButton>
        </Stack>
      </TableCell>
    </TableRow>
  );
};
export default CategoryRow;
