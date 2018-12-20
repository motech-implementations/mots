import React from 'react';

import CsvUpload from './csv-upload';

const CommunityUpload = () => (
  <div>
    <CsvUpload
      uploadUrl="/api/community/import"
    />
  </div>
);

export default CommunityUpload;
