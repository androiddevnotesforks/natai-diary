/**
 * Generated by @openapi-codegen
 *
 * @version 1.0.0
 */
export type StaticContentResponse = {
  terms: string;
};

export type ChangeEmailRequest = {
  newEmail: string;
};

export type ValidationErrorResponseRef = {
  /**
   * @example validation_error
   */
  code: string;
  errors: ValidationErrorItem[];
};

export type ChangeEmailErrorRef = {
  /**
   * @example already_exists
   */
  code: "already_exists";
};

export type ServerErrorRef = {
  /**
   * @example https://tools.ietf.org/html/rfc2616#section-10
   */
  type: string;
  /**
   * @example An error occurred
   */
  title: string;
  /**
   * @example 500
   */
  status: number;
  /**
   * @example Internal Server Error
   */
  detail: string;
};

export type ChangeEmailConfirmationRequest = {
  token: string;
};

export type ChangeEmailConfirmationErrorRef = {
  /**
   * @example token_not_found | token_expired
   */
  code: string;
};

export type ChangePasswordRequest = {
  oldPassword: string;
  newPassword: string;
};

export type AuthRequiredErrorResponse = {
  /**
   * @example https://tools.ietf.org/html/rfc2616#section-10
   */
  type: string;
  /**
   * @example An error occurred
   */
  title: string;
  /**
   * @example 401
   */
  status: number;
  /**
   * @example Unauthorized
   */
  detail: string;
};

export type ChangePasswordErrorRef = {
  /**
   * @example old_password_invalid
   */
  code: "old_password_invalid";
};

export type LoginRequestRef = {
  email: string;
  password: string;
};

export type LoginSuccessResponse = {
  user: UserDto;
  apiToken: string;
};

export type LoginErrorRef = {
  /**
   * @example Invalid credentials.
   */
  error: string;
};

export type PasswordResetRequest = {
  email: string;
};

export type ResetPasswordErrorRef = {
  /**
   * @example user_not_found
   */
  code: string;
};

export type PasswordResetConfirmationRequest = {
  token: string;
  password: string;
};

export type ResetPasswordConfirmationErrorRef = {
  /**
   * @example token_not_found | token_expired
   */
  code: string;
};

export type RegistrationRequest = {
  email: string;
  password: string;
  name: string;
};

export type RegistrationSuccessResponse = {
  userId: UuidV4;
};

export type RegistrationErrorResponseRef = {
  /**
   * @example already_exists
   */
  code: string;
};

export type UserInfoResponse = {
  user: UserDto;
};

export type VerifyEmailRequest = {
  token: string;
};

export type VerifyEmailResponseRef = {
  /**
   * @example token_not_found | token_expired
   */
  code: string;
};

export type UpdateNoteRequest = {
  title: string;
  content: string;
  /**
   * @format date-time
   */
  actualDate: string;
  /**
   * @format date-time
   */
  updatedAt: string;
  /**
   * @format date-time
   */
  deletedAt: string | null;
  tags: CloudTagDto[];
};

export type AccessDeniedErrorRef = {
  /**
   * @example https://tools.ietf.org/html/rfc2616#section-10
   */
  type: string;
  /**
   * @example An error occurred
   */
  title: string;
  /**
   * @example 403
   */
  status: number;
  /**
   * @example You can't delete this note, only owner can do it
   */
  detail: string;
};

export type NotFoundErrorRef = {
  /**
   * @example https://tools.ietf.org/html/rfc2616#section-10
   */
  type: string;
  /**
   * @example An error occurred
   */
  title: string;
  /**
   * @example 404
   */
  status: number;
  /**
   * @example Note with provided id not found
   */
  detail: string;
};

export type FindAllNotesResponse = {
  notes: CloudNoteDto[];
};

export type NewNoteRequest = {
  title: string;
  content: string;
  /**
   * @format date-time
   * @example 2022-11-05
   */
  actualDate: string;
  /**
   * @format date-time
   * @example null
   */
  deletedAt: string | null;
  tags: CloudTagDto[];
};

export type NewNoteResponse = {
  noteId: UuidV4;
};

export type ValidationErrorItem = {
  /**
   * @example This value is not a valid email address.
   */
  message: string;
  /**
   * @example [email]
   */
  path: string;
  /**
   * @example email
   */
  label: string;
};

export type UserDto = {
  id: UuidV4;
  email: string;
  isEmailVerified: boolean;
  name: string;
  roles: string[];
};

/**
 * @format uuid
 */
export type UuidV4 = string;

export type CloudTagDto = {
  tag: string;
  score: number | null;
};

export type CloudNoteDto = {
  id: Uuid;
  userId: Uuid;
  title: string | null;
  content: string | null;
  /**
   * @format date
   * @example 2021-01-01
   */
  actualDate: string;
  /**
   * @format date-time
   */
  createdAt: string;
  /**
   * @format date-time
   */
  updatedAt: string;
  /**
   * @format date-time
   */
  deletedAt: string | null;
  tags: CloudTagDto[];
};

/**
 * @format uuid
 */
export type Uuid = string;