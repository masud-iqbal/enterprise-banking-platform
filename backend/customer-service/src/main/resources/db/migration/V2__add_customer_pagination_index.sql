-- Supports customer listing ordered by
-- created_at DESC, id DESC.

CREATE INDEX idx_customers_created_at_id ON customers(created_at DESC, id DESC);