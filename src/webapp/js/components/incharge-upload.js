import React from 'react';

import CsvUpload from './csv-upload';

const InchargesUpload = () => (
  <div>
    <CsvUpload
      uploadUrl="/api/incharge/upload"
      selectText="Select uploaded Incharges"
      uploadLabel="CSV File Upload for Incharges"
    />
  </div>
);

export default InchargesUpload;
