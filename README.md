# News Publisher Contract

This repository contains a simple Hardhat project implementing a Solidity smart contract named `NewsPublisher`.

## Requirements
- Node.js 18+
- npm

## Setup
Install dependencies and run the Hardhat tests:

```bash
cd blockchain
npm install
npm test
```

Use `npm run compile` to compile the contract.

## Running Locally

Follow these steps to start both the blockchain backend and the Next.js frontend on your machine.

### 1. Start a Local Hardhat Network

```bash
cd blockchain
npx hardhat node
```

This command keeps a local blockchain running on `http://127.0.0.1:8545`.

### 2. Deploy the Contract

Open a new terminal window and deploy `NewsPublisher` to the local network:

```bash
cd blockchain
npx hardhat run scripts/deploy.js --network localhost
```

Copy the contract address printed to the console.

### 3. Launch the Frontend

```bash
cd ../frontend
npm install
export NEXT_PUBLIC_CONTRACT_ADDRESS=<address from step 2>
npm run dev
```

The app will be available at `http://localhost:3000` and can interact with the locally deployed contract.
