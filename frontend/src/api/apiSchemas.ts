/**
 * Generated by @openapi-codegen
 *
 * @version 1.0.0
 */
export type FeedbackRequest = {
  content: string;
  stars: number | null;
};

export type StaticContentResponse = {
  terms: string;
  privacy: string;
};

export type UploadAttachmentRequest = {
  filename: string;
};

export type SignedUploadUrl = {
  uploadUrl: string;
  attachmentId: UuidV4;
  /**
   * @format date-time
   */
  expiresAt: string;
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

export type ChangePasswordErrorRef = {
  /**
   * @example old_password_invalid
   */
  code: "old_password_invalid";
};

export type UserInfoResponse = {
  user: UserDto;
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

export type VerifyEmailRequest = {
  token: string;
};

export type VerifyEmailResponseRef = {
  /**
   * @example token_not_found | token_expired
   */
  code: string;
};

export type BuyFeatureResponse = {
  customerId: string;
  ephemeralKey: string | null;
  paymentIntentSecret: string | null;
};

export type BuySuggestionLinksErrorRef = {
  /**
   * @example empty_ephemeral_key
   */
  code: "empty_payment_intent_client_secret" | "empty_ephemeral_key";
};

export type UpdateNoteRequestV1 = {
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

export type FindAllLinkCategoriesResponse = {
  categories: LinkCategoryDto[];
};

export type FindAllNotesResponse = {
  notes: CloudNoteDto[];
};

export type NewNoteRequestV1 = {
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

export type FindAllSuggestionsResponse = {
  suggestions: CloudSuggestionDto[];
};

export type SuggestionLinksResponse = {
  links: SuggestionLinkDto[];
};

export type NoteAttachmentsResponse = {
  attachments: CloudAttachmentDto[];
};

export type GetNoteAttachmentsErrorRef = {
  /**
   * @example attachments_not_array
   */
  code: "attachments_not_array" | "attachments_not_uuids";
};

export type GetSuggestionLinksErrorRef = {
  /**
   * @example feature_not_available
   */
  code: "feature_not_available";
};

export type LoadLinkRequest = {
  url: string;
};

export type LoadLinkResponse = {
  html: string;
};

export type NewLinkRequest = {
  title: string;
  description: string;
  url: string;
  image: string | null;
  categories: number[];
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
  attachments: string[];
};

export type SuggestionFeedbackRequest = {
  rating: number;
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
  attachments: string[];
};

export type UploadLinkImageRequest = {
  imageUrl: string;
};

export type UploadLinkImageResponse = {
  url: string;
};

export type FindAllArticlesResponse = {
  articles: CloudBlogArticleDto[];
};

export type NewArticleRequest = {
  cover: string;
  translations: ArticleTranslationDto[];
  images: string[];
};

export type NewArticleResponse = {
  articleId: UuidV4;
  articleShortId: number;
};

export type ArticleResponse = {
  article: CloudBlogArticleDto;
};

export type EditArticleRequest = {
  cover: string;
  translations: ArticleTranslationDto[];
  images: string[];
};

/**
 * @format uuid
 */
export type UuidV4 = string;

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

export type CloudTagDto = {
  tag: string;
  score: number | null;
};

export type LinkCategoryDto = {
  id: number;
  name: string;
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
  attachments: string[];
};

export type CloudSuggestionDto = {
  id: UuidV4;
  notes: string[];
  suggestion: string;
  suggestionLinksCount: number;
  period: SuggestionPeriodDto;
  isReceived: boolean;
  feedbackRating: number | null;
  /**
   * @format date-time
   */
  createdAt: string;
};

export type SuggestionLinkDto = {
  id: number;
  url: string;
  title: string;
  description: string;
  image: string | null;
};

export type CloudAttachmentDto = {
  attachmentId: Uuid;
  signedUrl: string;
  key: string;
  originalFilename: string;
  metadata: CloudAttachmentMetadataDto;
  previews: CloudAttachmentPreviewDto[];
};

export type CloudBlogArticleDto = {
  id: Uuid;
  shortId: number;
  cover: string;
  translations: ArticleTranslationDto[];
};

export type ArticleTranslationDto = {
  locale: string;
  title: string;
  content: string;
  slug: string;
  metaKeywords: string;
  metaDescription: string;
};

/**
 * @format uuid
 */
export type Uuid = string;

export type SuggestionPeriodDto = {
  /**
   * @format date-time
   */
  from: string;
  /**
   * @format date-time
   */
  to: string;
};

export type CloudAttachmentMetadataDto = {
  mimeType: string | null;
  size: number | null;
  width: number | null;
  height: number | null;
};

export type CloudAttachmentPreviewDto = {
  key: string;
  signedUrl: string;
  width: number;
  height: number;
  type: string;
};
