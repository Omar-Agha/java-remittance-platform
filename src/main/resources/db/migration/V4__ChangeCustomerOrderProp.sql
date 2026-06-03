ALTER TABLE "address" RENAME TO customer_address;

ALTER TABLE customer_address 
RENAME COLUMN "order" TO sort_order;
