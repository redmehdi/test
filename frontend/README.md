# Frontend

This folder contains a simple Next.js app that interacts with the `NewsPublisher` smart contract.

## Setup

Install dependencies and run the development server:

```bash
cd frontend
npm install
npm run dev
```

Set the contract address after deploying the smart contract:

```bash
export NEXT_PUBLIC_CONTRACT_ADDRESS=0xYourContractAddress
```

The app allows wallet connection via MetaMask, article publication with IPFS storage, listing of all articles and one-click voting.
