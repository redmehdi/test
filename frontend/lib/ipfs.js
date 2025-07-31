import { create } from 'ipfs-http-client';

const client = create({ url: 'https://ipfs.infura.io:5001/api/v0' });

export async function upload(content) {
  const { path } = await client.add(content);
  return path;
}
