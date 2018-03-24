import React from 'react';

import CsvUpload from './csv-upload';

const InchargesUpload = () => (
  <div>
    <CsvUpload uploadUrl="/api/incharge/upload" />
  </div>
);

export default InchargesUpload;
