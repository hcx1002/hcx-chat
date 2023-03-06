import { getServiceEnvConfig } from '../env-config';
import { createRequest } from './request';

const { url } = getServiceEnvConfig('dev');

export const request = createRequest({ baseURL: url});
