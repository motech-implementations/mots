import React from 'react';

import CsvUpload from './csv-upload';

const ChiefdomUpload = () => (
  <div>
    <CsvUpload
      uploadUrl="/api/chiefdom/import"
    />
  </div>
);

export default ChiefdomUpload;
