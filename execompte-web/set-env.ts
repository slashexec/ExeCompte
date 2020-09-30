//import { writeFile } from 'fs';
const fs = require('fs');
// Configure Angular `environment.ts` file path
const targetPath = './src/environments/environment.ts';
// Load node modules
const colors = require('colors');
require('dotenv').config();
// `environment.ts` file structure
const envConfigFile = `export const environment = {
   apiUrl: '${process.env.API_URL}',
   platform: '${process.env.PLATFORM}',
   production: ${process.env.PRODUCTION}
};
`;
console.log(colors.magenta('The file `environment.ts` will be written with the following content: \n'));
console.log(colors.grey(envConfigFile));
fs.writeFile(targetPath, envConfigFile, function (err) {
   if (err) {
       throw console.error(err);
   } else {
       console.log(colors.magenta(`Angular environment.ts file generated correctly at ${targetPath} \n`));
   }
});
