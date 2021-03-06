import { useSnackbar } from "notistack";
import { useState } from "react";

import { MyModal } from "#root/components/MyModal";
import { useTypeSafeTranslation } from "#root/lib/hooks/useTypeSafeTranslation";

import CreateUserForm, { CreateUserFormPayload } from "./CreateUserForm";
import { RoleCode } from "./types";
import UserController from "./UserController";

type CreateUserModalProps = {
  isOpen: boolean;
  onClose: () => void;
  onCreateUser?: () => void;
};

const CreateUserModal = ({ isOpen, onClose, onCreateUser }: CreateUserModalProps) => {
  const { t } = useTypeSafeTranslation();
  const { enqueueSnackbar } = useSnackbar();

  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (payload: CreateUserFormPayload) => {
    setIsLoading(true);
    const [res, err] = await UserController.create({
      role: { code: payload.role as RoleCode },
      username: payload.username.trim(),
      password: payload.password.trim(),
      email: payload.email.trim(),
      name: payload.name.trim(),
      lastname: payload.lastname.trim(),
    });
    if (err || !res) {
      setIsLoading(false);
      enqueueSnackbar(err?.response?.data.mensaje || t("common.error"), { variant: "error" });
      return;
    }
    setIsLoading(false);
    enqueueSnackbar(`${t("common.createdSuccess")}`, { variant: "success" });
    onCreateUser?.();
    onClose();
  };

  return (
    <MyModal
      isOpen={isOpen}
      onClose={onClose}
      title={t("pages.user.createUser")}
      willCloseOnEsc={false}
    >
      <CreateUserForm isLoading={isLoading} onSubmit={handleSubmit} />
    </MyModal>
  );
};

export default CreateUserModal;
